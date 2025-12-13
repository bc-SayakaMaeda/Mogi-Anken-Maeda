# Web Template Application

## 概要 (Overview)

このプロジェクトは、Spring Bootをベースとした企業向けWebアプリケーションのテンプレートです。セキュアで保守性の高いWebアプリケーション開発のためのベストプラクティスと共通機能を実装しています。

This project is an enterprise web application template based on Spring Boot. It implements best practices and common features for developing secure and maintainable web applications.

## 何ができますか？ (What can you do?)

このテンプレートは以下の機能を提供します：

### 1. セキュリティ機能 (Security Features)
- **トークンベースのCSRF対策**: 二重送信防止とセッション管理
- **セキュアクッキー**: HTTPS環境でのセキュアなセッション管理
- **XSS対策**: X-XSS-Protectionヘッダーの自動設定
- **クリックジャッキング対策**: Content-Security-Policyヘッダーの設定

### 2. 例外処理 (Exception Handling)
- **統一された例外処理**: カスタム例外クラスによる階層的なエラー管理
  - `WebParamException`: パラメータ検証エラー
  - `WebUnexpectedException`: 予期しないシステムエラー
  - `WebViewHandlingException`: 画面表示用エラー
- **自動エラーページ遷移**: エラー発生時の適切な画面表示

### 3. ログ機能 (Logging)
- **MDCベースのログ管理**: リクエストごとの追跡可能なログ
- **レベル別ログ出力**: INFO、WARN、ERRORレベルでの適切なログ記録
- **画面ID連携**: `@AppDescription`アノテーションによる画面識別情報の自動ログ出力

### 4. バリデーション (Validation)
- **カスタムバリデーション**:
  - `@DatePattern`: 日付形式の検証
  - `@EnumPattern`: 列挙型の検証
- **Bean Validation統合**: Spring標準のバリデーション機能

### 5. データベース連携 (Database Integration)
- **Spring JDBC統合**: 名前付きパラメータJDBCTemplate
- **MyBatis Thymeleaf統合**: SQLファイルの動的生成
- **リトライ機能**: データベース接続エラー時の自動リトライ
- **コネクションプール**: Hikari CPによる効率的な接続管理

### 6. テンプレートエンジン (Template Engine)
- **Thymeleaf統合**: セキュアなHTML生成
- **エラーページカスタマイズ**: 404エラーやシステムエラーの専用ページ

## 技術スタック (Technology Stack)

- **Java**: 17
- **Spring Boot**: 3.3.1
- **フレームワーク**:
  - Spring Web MVC
  - Spring AOP
  - Spring JDBC
  - Spring Validation
  - Spring Retry
- **テンプレートエンジン**: Thymeleaf
- **データベース**: Microsoft SQL Server
- **ビルドツール**: Maven
- **その他**:
  - Lombok
  - MyBatis Thymeleaf
  - Logback

## プロジェクト構造 (Project Structure)

```
web-template/
├── src/
│   ├── main/
│   │   ├── java/jp/co/benesse/web/
│   │   │   ├── annotation/        # カスタムアノテーション
│   │   │   ├── common/            # 共通機能（トークン処理等）
│   │   │   ├── constants/         # 定数クラス
│   │   │   ├── controller/        # コントローラー
│   │   │   ├── entity/            # エンティティ
│   │   │   ├── enums/             # 列挙型
│   │   │   ├── exception/         # カスタム例外
│   │   │   ├── form/              # フォームクラス
│   │   │   ├── interceptor/       # インターセプター
│   │   │   ├── log/               # ログ関連
│   │   │   ├── repository/        # データアクセス層
│   │   │   ├── service/           # サービス層
│   │   │   ├── util/              # ユーティリティ
│   │   │   └── validation/        # バリデーター
│   │   └── resources/
│   │       ├── sql/               # SQLファイル
│   │       ├── templates/         # Thymeleafテンプレート
│   │       ├── application.properties
│   │       ├── messages.properties
│   │       └── logback-spring.xml
│   └── test/                      # テストコード
└── pom.xml
```

## ビルドと実行 (Build and Run)

### 前提条件 (Prerequisites)
- Java 17以上
- Maven 3.6以上
- Microsoft SQL Serverへのアクセス権限

### ビルド (Build)
```bash
# リポジトリのルートから実行する場合
cd web-template/web-template
mvn clean install
```

### 実行 (Run)
```bash
# web-template/web-template ディレクトリから実行
mvn spring-boot:run
```

### パッケージング (Packaging)
```bash
mvn clean package
```

WARファイルが `target/web-template-0.0.1-SNAPSHOT.war` に生成されます。

## 設定 (Configuration)

`src/main/resources/application.properties` でアプリケーションの設定を行います：

- **データベース接続**: `spring.datasource.*`
- **ログ設定**: `logging.config`
- **メッセージ設定**: `spring.messages.*`
- **キャッシュ設定**: `spring.web.resources.cache.*`

## サンプル機能 (Sample Features)

### サンプル画面
- **URL**: `/sample?id={会員ID}` (例: `/sample?id=123`)
- **機能**: 会員のオプト情報の表示と変更
- **セキュリティ**: トークンによる二重送信防止

このサンプルは、実装パターンのリファレンスとして利用できます。

## 開発ガイドライン (Development Guidelines)

1. **コントローラー**: `@AppDescription`アノテーションで画面IDと名称を設定
2. **例外処理**: 適切なカスタム例外を使用
3. **トークン管理**: 更新処理では必ずトークンの発行・検証・削除を実施
4. **ログ出力**: `LogUtil`クラスを使用
5. **メッセージ管理**: `messages.properties`で一元管理

## ライセンス (License)

このプロジェクトはBenesse Corporation向けの内部プロジェクトです。

## 作成者 (Author)

BC)yoda

---

**注意**: このテンプレートを使用する際は、`application.properties`のデータベース接続情報を適切に設定してください。
