## 概要
プロトタイプ詳細ページに、編集・削除・コメント機能を実装しました。

## 変更内容
- PrototypeController: 詳細表示・編集画面・更新・削除
- CommentController: コメント投稿
- PrototypeMapper / CommentMapper: 必要なCRUDメソッドを追加(アノテーション型)
- UserMapper: マイページ機能向けにupdateメソッドを追加
- PrototypeForm / CommentForm: バリデーション付き入力フォーム
- prototypes/detail.html, prototypes/edit.html: Thymeleafテンプレート新規作成
- build.gradle: spring-boot-starter-validation を追加

## 認可
- 詳細ページは未ログインでも閲覧可能
- 編集・削除リンクは投稿者本人にのみ表示
- 編集・削除・コメント投稿は投稿者/ログインユーザーのみ実行可能(Controller側で判定)

## テスト
- PrototypeControllerTest: 全件パス
- CommentControllerTest: 全件パス
