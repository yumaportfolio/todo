# お知らせ管理システム

Spring Boot + PostgreSQLで構築されたお知らせ管理アプリケーションです。

## 機能

- お知らせの登録・更新・削除
- 多様な検索機能（タイトル、区分、日付範囲）
- ページング対応の一覧表示
- クライアント/サーバー両側のバリデーション
- 日付範囲チェック機能

## 技術スタック

- **Java**: 21
- **Spring Boot**: 3.5.7
- **データベース**: PostgreSQL 16
- **テンプレートエンジン**: Thymeleaf
- **ビルドツール**: Gradle
- **コンテナ**: Docker & Docker Compose

---

## クイックスタート

### 前提条件

以下のいずれかをインストール：
- **Docker Desktop**（推奨 - 最も簡単）
- または **Java 21 + PostgreSQL**

### 方法A: Docker Composeで起動（推奨）

```bash
# 1. リポジトリをクローン
git clone <repository-url>
cd todo

# 2. データベースを起動
docker-compose up -d db

# 3. アプリケーションを起動（10秒待機後）
sleep 10
./gradlew bootRun
```

**アクセス**: http://localhost:8080

### 方法B: 完全Docker環境

```bash
# すべてDocker Composeで起動
docker-compose -f docker-compose.prod.yml up -d
```

**アクセス**: http://localhost:8080

---

## 開発環境セットアップ

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd todo
```

### 2. データベースの起動

```bash
# Docker Composeでデータベース起動
docker-compose up -d db

# 起動確認（STATUSがUpになっていればOK）
docker-compose ps
```

### 3. アプリケーションの起動

#### IntelliJ IDEAから起動（推奨）

1. `src/main/java/com/example/todo/TodoApplication.java` を開く
2. クラス名の左の▶️をクリック
3. 「Run 'TodoApplication'」を選択

#### コマンドラインから起動

```bash
./gradlew bootRun
```

### 4. アクセス

ブラウザで http://localhost:8080 を開く

---

## 他の環境での動作確認

### 他のPCで動作確認

```bash
# GitHubからクローン
git clone <repository-url>
cd todo

# Docker Composeで起動
docker-compose -f docker-compose.prod.yml up -d

# アクセス
# http://localhost:8080
```

### VPS/クラウドサーバーでのデプロイ

```bash
# サーバーにSSH接続
ssh user@server-ip

# Dockerインストール
curl -fsSL https://get.docker.com | sh

# リポジトリクローン
git clone <repository-url>
cd todo

# 環境変数設定（本番環境用）
cp .env.prod.example .env
nano .env  # パスワードを変更

# 起動
docker-compose -f docker-compose.prod.yml up -d

# ファイアウォール設定
sudo ufw allow 8080

# アクセス
# http://server-ip:8080
```

### インターネット経由で共有（ngrok）

```bash
# ローカルで起動
./gradlew bootRun

# 別ターミナルでngrok起動
ngrok http 8080

# 表示されたURLを共有
# https://xxxx-xxx-xxx.ngrok-free.app
```

---

## プロジェクト構成

```
todo/
├── src/
│   ├── main/
│   │   ├── java/com/example/todo/
│   │   │   ├── controller/          # コントローラー層
│   │   │   │   ├── form/            # フォームオブジェクト
│   │   │   │   └── validation/      # バリデーション設定
│   │   │   ├── service/             # サービス層
│   │   │   │   └── query/           # 検索条件
│   │   │   ├── repository/          # リポジトリ層
│   │   │   ├── domain/              # エンティティ・列挙型
│   │   │   └── TodoApplication.java # メインクラス
│   │   └── resources/
│   │       ├── templates/           # Thymeleafテンプレート
│   │       ├── static/js/           # JavaScript
│   │       ├── application.properties
│   │       └── messages.properties
│   └── test/                        # テストコード
├── doc/                             # 設計ドキュメント
├── docker-compose.yml               # 開発環境用Docker設定
├── docker-compose.prod.yml          # 本番環境用Docker設定
├── Dockerfile                       # Dockerイメージ定義
├── build.gradle                     # Gradle設定
└── README.md
```

---

## テスト・ビルド

### テスト実行

```bash
./gradlew test
```

### ビルド

```bash
./gradlew build
```

JARファイルは `build/libs/todo-0.0.1-SNAPSHOT.jar` に生成されます。

### JARファイルで起動

```bash
# ビルド
./gradlew bootJar

# データベース起動
docker-compose up -d db
sleep 10

# JAR実行
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

---

## セキュリティ

### 機密情報の管理

このプロジェクトでは、以下のファイルに機密情報を含めないようにしています：

| ファイル | 公開 | 説明 |
|---------|------|------|
| `application.properties` | ✅ 公開 | 環境変数使用、デフォルト値は開発用 |
| `docker-compose.yml` | ✅ 公開 | 環境変数使用、デフォルト値は開発用 |
| `.env` | ❌ 除外 | 実際のパスワード（.gitignore） |
| `application-dev.properties` | ❌ 除外 | ローカル設定（.gitignore） |

### デフォルト値について

**開発環境用のデフォルト値**:
- ユーザー名: `user`
- パスワード: `password`
- データベース名: `todo_db`

**本番環境では必ず環境変数で設定してください**

```bash
export DB_USERNAME=prod_user
export DB_PASSWORD=STRONG_PASSWORD_HERE
./gradlew bootRun
```

---

## トラブルシューティング

### ポート8080が既に使用されている

```bash
# 使用中のプロセスを停止
lsof -ti:8080 | xargs kill -9

# または、別のポートを使用
java -Dserver.port=8081 -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

### データベース接続エラー

```bash
# データベースを再起動
docker-compose down -v
docker-compose up -d db
sleep 10
./gradlew bootRun
```

### ビルドエラー

```bash
# クリーンビルド
./gradlew clean build

# Gradleキャッシュをクリア
./gradlew --stop
./gradlew clean build
```

---

## コントリビューション

1. このリポジトリをフォーク
2. 新しいブランチを作成 (`git checkout -b feature/amazing-feature`)
3. 変更をコミット (`git commit -m 'Add amazing feature'`)
4. ブランチにプッシュ (`git push origin feature/amazing-feature`)
5. プルリクエストを作成

---

## ライセンス

このプロジェクトは学習・研修目的で作成されています。

## 作成者

伊東

---

**最終更新日**: 2025年11月25日

