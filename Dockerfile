# マルチステージビルドでサイズを最適化
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Gradleラッパーと設定ファイルをコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 依存関係を事前ダウンロード（キャッシュ活用）
RUN ./gradlew dependencies --no-daemon || true

# ソースコードをコピー
COPY src src

# アプリケーションをビルド（テストはスキップ）
RUN ./gradlew bootJar -x test --no-daemon

# ===================================
# 実行用の軽量イメージ
# ===================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 非rootユーザーでアプリケーションを実行（セキュリティ向上）
RUN addgroup -g 1001 appgroup && \
    adduser -D -u 1001 -G appgroup appuser

# ビルド成果物をコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# ユーザーを切り替え
USER appuser

# ヘルスチェック用エンドポイント（Spring Boot Actuatorがある場合）
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

# アプリケーションポートを公開
EXPOSE 8080

# JVMオプションを環境変数で設定可能に
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# アプリケーション起動
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

