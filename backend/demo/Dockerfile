# ベースイメージを指定します。Java 17を使用します。
FROM openjdk:17-jdk-alpine

# 作業ディレクトリを設定します。
WORKDIR /

# Mavenの依存関係をコピーしてビルドします。
COPY target/demo-0.0.1-SNAPSHOT.jar /demo.jar

# SQLiteのデータベースファイルをコピーします。
COPY db/database.tinyurl /db/database.tinyurl

# ポート番号を公開します（Spring Bootのデフォルトポートは8080です）。
EXPOSE 8080

# Install SQLite3
RUN apk update && apk add sqlite

# アプリケーションを起動します。
CMD ["java", "-jar", "demo.jar", "--spring.datasource.url=jdbc:sqlite:/db/database.tinyurl"]