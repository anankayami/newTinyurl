 ## Shortener URL
This is a URL Shortener application built using Quasar for the frontend and Spring Boot for the backend. The application uses SQLite as the database.
###   Requisites
######      ・Java 
######      ・JavaScript
######      ・Spring boot
######      ・Mybatis
######      ・Maven
######      ・Quasar
######      ・Vue
######      ・SQLite
######      ・Redis

### Getting start
####    1.clone project 
  ```
    git clone https://github.com/anankayami/newTinyurl.git
  ```

####    2.using dockerfile for backend Build and Run ,redis build
```
      cd backend/demo
```
```
     mvn clean package
```
```
      docker-compose up --build
```
###### stop container
```
      docker-compose down 
```
#### The backend server should start at http://localhost:8080.

####     3.start frontend 
#####       ・Navigate to the backend directory:
```
      $ cd frontend
```
#####    ・Install dependencies:
```
      npm install
```
#####    ・Start the Quasar development server:
```
      npx quasar dev
  ```
####  The frontend should be available at http://localhost:8082.
####    4.Database
###### The application uses SQLite as the database. The database file is located at backend/demo/db/database.tinyurl
##### 4.1 login SQLite
```
     cd backend/demo/db
     sqlite3 database.tinyurl
```
##### 4.2 eixt SQLite
```
    .exit
```
##### 4.3 login Redis
```
  redis-cli
```
##### 4.4 get Redis data
```
  get <key>
```
###### eg. get 「original URl」  or get 「shortUrl」

------------------------------------------------------------



