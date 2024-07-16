# ベースイメージを指定します。Java 17を使用します。
FROM openjdk:17-jdk-alpine

# 作業ディレクトリを設定します。
WORKDIR /app

# Mavenの依存関係をコピーしてビルドします。
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# SQLiteのデータベースファイルをコピーします。
COPY db/database.tinyurl /app/db/database.tinyurl

# ポート番号を公開します（Spring Bootのデフォルトポートは8080です）。
EXPOSE 8080

# アプリケーションを起動します。
CMD ["java", "-jar", "demo.jar", "--spring.datasource.url=jdbc:sqlite:/app/db/database.tinyurl"]