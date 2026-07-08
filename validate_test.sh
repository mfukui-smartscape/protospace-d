#!/usr/bin/env bash
#
# validate_test.sh - protospace-d 本番 フォームバリデーション自動テスト (案B)
#
# CSRFトークンとセッションCookieを扱い、実際にPOSTして
# 「200=バリデーションで弾かれた / 302=成功でリダイレクト」で各項目を判定する。
#
# 使い方:
#   ./validate_test.sh
#   BASE_URL=http://localhost:8080 ./validate_test.sh   # ローカル対象
#
# 依存: bash, curl, grep, sed  (macOS標準/Linux標準でOK)
#

set -u
BASE_URL="${BASE_URL:-https://protospace-d.onrender.com}"

GREEN=$'\e[32m'; RED=$'\e[31m'; YELLOW=$'\e[33m'
BOLD=$'\e[1m'; DIM=$'\e[2m'; CYAN=$'\e[36m'; RESET=$'\e[0m'

pass=0; fail=0
COOKIE_JAR="$(mktemp)"
trap 'rm -f "$COOKIE_JAR"' EXIT

echo
echo "${BOLD}protospace-d フォームバリデーションテスト${RESET}"
echo "${DIM}対象: ${BASE_URL}${RESET}"
echo "${DIM}$(date '+%Y-%m-%d %H:%M:%S')${RESET}"

# ─────────────────────────────────────────────
# ヘルパー: 指定URLのフォームページを取得し _csrf トークンを取り出す
#   Cookieも $COOKIE_JAR に保存される
# 出力: トークン文字列(取れなければ空)
# ─────────────────────────────────────────────
get_csrf() {
  local url="$1"
  curl -s -c "$COOKIE_JAR" -b "$COOKIE_JAR" --max-time 30 "$url" 2>/dev/null \
    | grep -o 'name="_csrf"[^>]*value="[^"]*"' \
    | sed -E 's/.*value="([^"]*)".*/\1/' \
    | head -n1
}

# ─────────────────────────────────────────────
# ヘルパー: POSTしてHTTPステータスコードを返す
#   引数: URL, 追加の -d フィールド (可変)
# ─────────────────────────────────────────────
post_status() {
  local url="$1"; shift
  curl -s -o /dev/null -w '%{http_code}' \
    -c "$COOKIE_JAR" -b "$COOKIE_JAR" \
    --max-time 30 \
    -X POST "$@" "$url" 2>/dev/null
}

# 結果表示
report() {
  local ok="$1" label="$2" detail="$3"
  if [ "$ok" = "1" ]; then
    printf ' %s✓%s  %-46s %s%s%s\n' "$GREEN" "$RESET" "$label" "$DIM" "$detail" "$RESET"
    pass=$((pass+1))
  else
    printf ' %s✗%s  %-46s %s%s%s\n' "$RED" "$RESET" "$label" "$YELLOW" "$detail" "$RESET"
    fail=$((fail+1))
  fi
}

section() { echo; echo "${CYAN}${BOLD}$1${RESET}"; echo "────────────────────────────────────────────────────"; }

# 有効な基準データ (全項目正しい値)。テストごとに一部を欠落/改変させる。
V_EMAIL_BASE="tester"
V_PASS="password123"
V_NAME="テスト太郎"
V_PROFILE="プロフィール"
V_AFFIL="所属テスト"
V_POSITION="役職テスト"

# ユニークなメールを生成 (本番DBを汚しても重複しないように)
uniq_email() { echo "${V_EMAIL_BASE}$(date +%s)$RANDOM@example.com"; }

# ─────────────────────────────────────────────
# 登録POSTを1回行い、ステータスを返す共通関数
#   引数はすべて "field=value" 形式。CSRFトークンは自動付与。
# ─────────────────────────────────────────────
register_post() {
  local token
  token=$(get_csrf "$BASE_URL/users/new")
  local args=(-d "_csrf=$token")
  local f
  for f in "$@"; do args+=(-d "$f"); done
  post_status "$BASE_URL/users" "${args[@]}"
}

# 期待: バリデーションで弾かれる → 200 (users/new 再表示)
expect_rejected() {
  local label="$1"; shift
  local code; code=$(register_post "$@")
  # 200 = 弾かれてフォーム再表示 / 302 = 通ってしまった(NG)
  if [ "$code" = "200" ]; then report 1 "$label" "[200 留まった]"; else report 0 "$label" "[got $code, want 200]"; fi
}

# 期待: 登録成功 → 302 (/login へ)
expect_accepted() {
  local label="$1"; shift
  local code; code=$(register_post "$@")
  if [ "$code" = "302" ]; then report 1 "$label" "[302 登録成功]"; else report 0 "$label" "[got $code, want 302]"; fi
}

# ═════════════════════════════════════════════
# 疎通・認可 (GETベース)
# ═════════════════════════════════════════════
section "疎通・認可"

get_status() { curl -s -o /dev/null -w '%{http_code}' --max-time 30 "$BASE_URL$1" 2>/dev/null; }

c=$(get_status "/");            [ "$c" = "200" ] && report 1 "トップページが開く" "[200]" || report 0 "トップページが開く" "[got $c]"
c=$(get_status "/users/new");   [ "$c" = "200" ] && report 1 "登録ページが開く(未ログイン)" "[200]" || report 0 "登録ページが開く(未ログイン)" "[got $c]"
c=$(get_status "/login");       [ "$c" = "200" ] && report 1 "ログインページが開く" "[200]" || report 0 "ログインページが開く" "[got $c]"
c=$(get_status "/prototypes/new"); [ "$c" = "302" ] && report 1 "投稿ページは未ログインでlogin送り" "[302]" || report 0 "投稿ページは未ログインでlogin送り" "[got $c]"

# CSS content-type
ct=$(curl -s -o /dev/null -w '%{content_type}' --max-time 30 "$BASE_URL/css/style.css" 2>/dev/null)
case "$ct" in text/css*) report 1 "CSSがCSSとして配信される" "[$ct]";; *) report 0 "CSSがCSSとして配信される" "[got ${ct:-none}]";; esac

# ═════════════════════════════════════════════
# ユーザー登録バリデーション (各項目を1つずつ欠落させて弾かれるか)
# ═════════════════════════════════════════════
section "ユーザー登録バリデーション"

# 基準となる有効フィールド一式を組む関数 (emailだけ都度ユニーク)
EMAIL="$(uniq_email)"
valid_fields() {
  echo "email=$EMAIL"
  echo "password=$V_PASS"
  echo "passwordConfirmation=$V_PASS"
  echo "name=$V_NAME"
  echo "profile=$V_PROFILE"
  echo "affiliation=$V_AFFIL"
  echo "position=$V_POSITION"
}

# email 空 → 弾かれる
expect_rejected "メール必須 (空で弾かれる)" \
  "email=" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# email @なし → 弾かれる
expect_rejected "メール形式 (@なしで弾かれる)" \
  "email=invalid-email" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# password 空 → 弾かれる
expect_rejected "パスワード必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=" "passwordConfirmation=" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# password 5文字 → 弾かれる
expect_rejected "パスワード6文字未満 (弾かれる)" \
  "email=$(uniq_email)" "password=abc12" "passwordConfirmation=abc12" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# password不一致 → 弾かれる
expect_rejected "パスワード確認 不一致 (弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=different99" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# passwordConfirmation 空 → 弾かれる
expect_rejected "パスワード確認 必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# name 空 → 弾かれる
expect_rejected "ユーザー名必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# profile 空 → 弾かれる
expect_rejected "プロフィール必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=" "affiliation=$V_AFFIL" "position=$V_POSITION"

# affiliation 空 → 弾かれる
expect_rejected "所属必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=" "position=$V_POSITION"

# position 空 → 弾かれる
expect_rejected "役職必須 (空で弾かれる)" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position="

# 全項目正しい → 登録成功 (302)  ※本番DBにユニークユーザーが1件増える
expect_accepted "全項目正しいと登録成功する" \
  "email=$(uniq_email)" "password=$V_PASS" "passwordConfirmation=$V_PASS" "name=$V_NAME" "profile=$V_PROFILE" "affiliation=$V_AFFIL" "position=$V_POSITION"

# ═════════════════════════════════════════════
# 集計
# ═════════════════════════════════════════════
echo
echo "════════════════════════════════════════════════════"
total=$((pass+fail))
if [ "$fail" -eq 0 ]; then
  echo " ${GREEN}${BOLD}ALL PASS${RESET}  (${pass}/${total})"
else
  echo " ${RED}${BOLD}${fail} FAILED${RESET}  ${GREEN}${pass} passed${RESET}  (計 ${total})"
fi
echo
echo " ${DIM}※「全項目正しいと登録成功」を含めた場合、本番DBにテストユーザーが1件増えます${RESET}"
echo " ${DIM}※ ヘッダー表示/画像リンク切れ/画面遷移など目視項目は checklist.html で手動確認${RESET}"
echo

[ "$fail" -eq 0 ]