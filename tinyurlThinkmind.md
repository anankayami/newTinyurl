# Shorten URL Service

## Skill
- Frontend
  - Vue 3
  - Quasar
  - Pinia
- Backend
  - Spring Boot
  - MyBatis
  - SQLite
  - Lombok
  - Maven
  - Redis
- Deployment
  - Docker
  - commandline

## Function
- Estimate
  - 1. Storage capacity: 12 billion short URLs, approximately 12 TB of storage space
  - 2. Throughput : an average of 20,000 QPS ,peak times reaching 40,000 QPS
  - 3. Network Bandwith : 320Mb during peak hours
  - 4. short URL length: 6 characters
- Short URL Generation Algorithm
  - 1. Hash function
  - 2. Incremental Short URL
  - 3. Pre-generated URLs
- Pre-generated URLs
  - ①generate 6 characters URLs and load
  - ②accept a long URL and check invalid or duplicate URLs
  - ③get a unique shortened URL from load table and return
- Redirect the shortened URL to the original long URL
- RESTful API
  - Post /api/preGenerate
  - Post /api/preReload
  - Post /api/deleteOldUrls
  - Post /api/shorten
  - Get /api/{shortUrl}
  - Get /api/{shortUrl}/clicks
- Scheduled event
  - preload data into the pre_reload_urls table every midnight
  - delete data older than 2 years every midnight

## No Function
- Scalability and Efficiency
  - Nginx Load Balancer
  - more than one backend service
  - redis cache
  - pre-generated URLs
- Unpredictablity
  - pre-generated URLs
- Deployment
  - Setting Dockerfile and Docker-compose file
    - containers
    - images
    - add volumes to persist the data

## Database SQLite
- Table: pre_generated_urls
- Table: pre_reload_urls
- Table: urls
  
## Cache - Redis
- key: shortened URL , value: original long URL
- key: original long URl , value: shortened URL

## Version manager
- Git
- GitHub

## Add information
- upgrade
  - hdfs file for pre-generated URLs
  - NoSQL
  - jmeter test,unit test,api test etc
  - Google Cloud Run or Knative