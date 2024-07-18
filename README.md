 ## Shortener URL
This is a URL Shortener application built using Quasar for the frontend and Spring Boot for the backend. The application uses SQLite as the database.
###   Requisites
######      ・Java 
######      ・Spring boot
######      ・Maven
######      ・Quasar
######      ・Vue
######      ・SQLite

### Getting start
####    1.clone project 
  ```
    git clone https://github.com/anankayami/newTinyurl.git
  ```

####    2.redis build
```
      cd backend/demo
```
```
      docker-compose up --build
```
###### stop container
```
      docker-compose down 
```

####     3. or backend Build and Run local
#####       Navigate to the backend directory:
```
      cd backend/demo
```
#####    ・Build the Spring Boot application:
```
      ./mvnw clean package
```
#####    ・　Run the application:
```
      java -jar target/demo-0.0.1-SNAPSHOT.jar
```
#### The backend server should start at http://localhost:8080.

####     4.start frontend 
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
####    5.Database
###### The application uses SQLite as the database. The database file is located at backend/demo/db/database.tinyurl
------------------------------------------------------------



