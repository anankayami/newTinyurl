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
######      ・Nginx

### Getting start
####    1.clone project 
```
git clone https://github.com/anankayami/newTinyurl.git
```

####    2.start server
```
docker-compose up --build
```
###### confirm container
```
docker ps -a
```
### eg.
```
NAMES
newtinyurl_frontend_1
newtinyurl_backend1_1
newtinyurl_backend2_1
newtinyurl_redis-server_1
```

###### stop container
```
docker-compose down 
```

#### The backend server should start at http://localhost:8080 and http://localhost:8081.

####  The frontend should be available at http://localhost:8082.

---------------------------------------------------------------------------------

####    4.Database
###### The application uses SQLite as the database. The database file is located at backend/demo/db/database.tinyurl
#####   4.1 login container
```
docker ps
docker exec -it newtinyurl_backend1_1 sh
```
#####   4.2 login SQLite
```
cd db
sqlite3 database.tinyurl
```
#####   4.3 eixt SQLite
```
.exit
```
#####   4.4 logout container
```
exit
```

#### 5. redis
#####   5.1 login container
```
docker ps
docker exec -it demo_redis-server_1 sh
```
#####   5.2 login Redis
```
redis-cli
```
#####   5.3 get Redis data
```
get <key>
```
###### eg. get 「original URl」  or get 「shortUrl」

#####   5.4 show all Redis data
```
KEYS *
```
#####   5.5  logout redis
```
exit
```
#####   5.6  logout constainer
```
exit
```

------------------------------------------------------------



