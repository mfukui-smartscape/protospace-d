#!/usr/bin/env bash
#
# smoke_test.sh - protospace-d 本番スモークテスト
#
# 本番の主要エンドポイントを叩いて、期待するステータスが返るかを
# 色付きチェックリストで表示する。今日詰まったポイントを回帰チェックとして収録。
#
# 使い方:
#   ./smoke_test.sh                 # 本番(onrender)をチェック
#   BASE_URL=http://localhost:8080 ./smoke_test.sh   # ローカルをチェック
#

set -u

BASE_URL="${BASE_URL:-https://protospace-d.onrender.com}"

# 色
GREEN=$'\e[32m'
RED=$'\e[31m'
YELLOW=$'\e[33m'
BOLD=$'\e[1m'
DIM=$'\e[2m'
RESET=$'\e[0m'

pass_count=0
fail_count=0

echo
echo "${BOLD}protospace-d スモークテスト${RESET}"
echo "${DIM}対象: ${BASE_URL}${RESET}"
echo "${DIM}$(date '+%Y-%m-%d %H:%M:%S')${RESET}"
echo "────────────────────────────────────────────"

# check <ラベル> <メソッド> <パス> <期待ステータス(| 区切り可)>
check() {
  local label="$1" method="$2" path="$3" expected="$4"
  local url="${BASE_URL}${path}"

  # -s 静か / -o 本文捨てる / -w ステータスだけ取得 / -L はあえて付けずリダイレクトを検出
  # --max-time でスリープ復帰待ちの無限待ちを防ぐ
  local code
  code=$(curl -s -o /dev/null -w '%{http_code}' -X "$method" --max-time 30 "$url" 2>/dev/null)

  # 期待値に含まれるか判定 ( | 区切り )
  local ok=0
  local IFS='|'
  for e in $expected; do
    [ "$code" = "$e" ] && ok=1
  done

  if [ "$ok" = "1" ]; then
    printf ' %s✓%s  %-42s %s[%s]%s\n' "$GREEN" "$RESET" "$label" "$DIM" "$code" "$RESET"
    pass_count=$((pass_count + 1))
  else
    printf ' %s✗%s  %-42s %s[got %s, want %s]%s\n' "$RED" "$RESET" "$label" "$YELLOW" "$code" "$expected" "$RESET"
    fail_count=$((fail_count + 1))
  fi
}

# CSSが「CSSとして」配信されているか(Content-Type確認: loginにリダイレクトされてHTMLが返る事故の回帰)
check_css() {
  local label="$1" path="$2"
  local url="${BASE_URL}${path}"
  local ctype
  ctype=$(curl -s -o /dev/null -w '%{content_type}' --max-time 30 "$url" 2>/dev/null)

  case "$ctype" in
    text/css*)
      printf ' %s✓%s  %-42s %s[%s]%s\n' "$GREEN" "$RESET" "$label" "$DIM" "$ctype" "$RESET"
      pass_count=$((pass_count + 1))
      ;;
    *)
      printf ' %s✗%s  %-42s %s[got %s]%s\n' "$RED" "$RESET" "$label" "$YELLOW" "${ctype:-none}" "$RESET"
      fail_count=$((fail_count + 1))
      ;;
  esac
}

# ── 公開ページ (未ログインで開けるべき) ──
check "トップページが開く"                 GET /             200
check "登録ページが開く(未ログイン)"        GET /users/new   200
check "ログインページが開く"               GET /login       200

# ── 静的ファイル ──
check_css "CSSがCSSとして配信される"        /css/style.css

# ── 認証必須ページ (未ログインは弾かれるべき = 302でloginへ) ──
check "投稿ページは未ログインで弾かれる"     GET /prototypes/new  302

echo "────────────────────────────────────────────"
total=$((pass_count + fail_count))
if [ "$fail_count" -eq 0 ]; then
  echo " ${GREEN}${BOLD}ALL PASS${RESET}  (${pass_count}/${total})"
else
  echo " ${RED}${BOLD}${fail_count} FAILED${RESET}  ${GREEN}${pass_count} passed${RESET}  (計 ${total})"
fi
echo

# 失敗があれば非0終了 (CIやデプロイ後チェックで使えるように)
[ "$fail_count" -eq 0 ]