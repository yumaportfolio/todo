# お知らせToDo管理システム

Spring Boot + PostgreSQLで構築されたお知らせ管理アプリケーションです。

## 機能

- お知らせの登録・更新・削除
- 条件による検索機能
- ページング対応の一覧表示
- 日付範囲バリデーション

## 技術スタック

- **Java**: 21
- **Spring Boot**: 3.5.7
- **データベース**: PostgreSQL 16
- **テンプレートエンジン**: Thymeleaf
- **ビルドツール**: Gradle

## セットアップ

### 前提条件

- Java 21以上
- Docker & Docker Compose

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd todo
```

### 2. 環境変数の設定

開発環境用の設定ファイルを作成します：

#### .envファイルの作成（Docker用）

```bash
cp .env.example .env
```

`.env`ファイルを編集して、データベースのパスワードを設定：

```env
DB_USER=user
DB_PASSWORD=your_password_here
DB_NAME=todo_db
```

#### application-dev.propertiesの作成（Spring Boot用）

```bash
cp src/main/resources/application.properties.example src/main/resources/application-dev.properties
```

`application-dev.properties`を編集して、データベース接続情報を設定：

```properties
spring.datasource.url=jdbc:postgresql://localhost:25432/todo_db
spring.datasource.username=user
spring.datasource.password=your_password_here
```

### 3. データベースの起動

```bash
docker-compose up -d db
```

データベースが起動したことを確認：

```bash
docker-compose ps
```

### 4. アプリケーションの起動

#### 方法1: IDEから起動（推奨）

IntelliJ IDEAで`TodoApplication.java`を右クリックして「Run」を選択

起動時にプロファイルを指定：
- VM Options: `-Dspring.profiles.active=dev`

#### 方法2: Gradleコマンドから起動

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 5. アプリケーションへのアクセス

ブラウザで以下のURLにアクセス：

```
http://localhost:8080
```

自動的に`/notice`にリダイレクトされ、お知らせ一覧画面が表示されます。

## プロジェクト構成

```
todo/
├── src/
│   ├── main/
│   │   ├── java/com/example/todo/
│   │   │   ├── controller/       # コントローラー層
│   │   │   ├── service/          # サービス層
│   │   │   ├── repository/       # リポジトリ層
│   │   │   ├── domain/           # エンティティ・列挙型
│   │   │   └── TodoApplication.java
│   │   └── resources/
│   │       ├── templates/        # Thymeleafテンプレート
│   │       ├── static/js/        # JavaScript
│   │       └── application.properties
│   └── test/                     # テストコード
├── doc/                          # 設計ドキュメント
├── docker-compose.yml            # Docker設定
├── build.gradle                  # Gradle設定
└── README.md
```

## テスト実行

```bash
./gradlew test
```

## ビルド

```bash
./gradlew build
```

成果物は`build/libs/`に生成されます。

## セキュリティ

### 機密情報の管理

このプロジェクトでは、以下のファイルに機密情報を含めないようにしています：

- ✅ `application.properties` - 環境変数を使用
- ✅ `docker-compose.yml` - 環境変数を使用
- ❌ `.env` - .gitignoreで除外（Gitで管理しない）
- ❌ `application-dev.properties` - .gitignoreで除外

詳細は`SECURITY_CHECKLIST.md`を参照してください。

## コントリビューション

1. このリポジトリをフォーク
2. 新しいブランチを作成 (`git checkout -b feature/amazing-feature`)
3. 変更をコミット (`git commit -m 'Add amazing feature'`)
4. ブランチにプッシュ (`git push origin feature/amazing-feature`)
5. プルリクエストを作成

## ライセンス

このプロジェクトは学習・研修目的で作成されています。

## 作成者

伊東

---

**最終更新日**: 2025年11月25日

