# 基本となるNode.jsのイメージを指定
FROM node:18

# 作業ディレクトリを作成
WORKDIR /app

# package.jsonとpackage-lock.jsonをコピー
COPY package*.json ./

# 依存関係をインストール
RUN npm install

# プロジェクトのソースコードをコピー
COPY . .

# Quasarのビルドを実行
RUN npm run build

# Nginxのイメージを指定（QuasarのSPAを提供するため）
FROM nginx:alpine

# ビルドされたファイルをNginxの公開ディレクトリにコピー
COPY --from=0 /app/dist/spa /usr/share/nginx/html

# Nginxをポート80で起動
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
