# Feed Scraper

This is a simple project that can fetch feed's data from different source, supporting different protocols (**Atom, RSS**).

## API
**Subscribe Feed:**
```shell  
curl -X POST "http://127.0.0.1:8080/feed/subscribe" \
	--header "Content-Type: application/json" \
	--data '{"userId":"1", "url":"https://rss.art19.com/apology-line"}'
```
**Unsubscription Feed:**
```shell  
curl -X PUT http://127.0.0.1:8080/feed/unSubscribe \
  	--header "Content-Type: application/json" \
	--data '{"userId":"1", "feedId":"1"}'	  
```
**Feed Update :**
```shell  
curl -X PUT http://127.0.0.1:8080/feed/update \  
	--header "Content-Type: application/json"\
	--data '{"userId":"1", "feedId":"8"}'
```

**Get Feeds From Specific User :**
```shell
curl "http://127.0.0.1:8080/users/{userId}/feeds?fetchSize=15&toDate=2022-07-11%2012:00:00"
```
**Get Specific Feed Data:**
```shell
curl "http://127.0.0.1:8080/users/2/feeds/7/items?fetchSize=15&toDate=2022-07-11%2012:00:00"
```
**Bookmark Specific FeedItem:**
```shell  
curl -X PUT "http://127.0.0.1:8080/feed-item/bookmark" \
	--data '{"userId":"2", "feedId":"7", "feedItemId":"14423"}' \
	--header "Content-Type: application/json"
```
**UnBookmark Specific FeedItem:**
```shell
curl -X PUT "http://127.0.0.1:8080/feed-item/unBookmark" \
	--data '{"userId":"2", "feedId":"7", "feedItemId":"14423"}' \
	--header "Content-Type: application/json"
```
**Get Bookmark FeedItems:**
```shell
curl "http://127.0.0.1:8080//users/1/bookmarked"
```

## Set up
1. Before running the project, it's necessary to create DB schema. I used JOOQ framework to work with the database, And it needs some classes to be generated at compile time. So for building the project, it is necessary to run **00_create_tables.sql** in a PostgreSQL DB.
2. Then change **build.gradle** by replacing your database date
```groovy
jooq {
  version = '3.16.5'
  edition = nu.studer.gradle.jooq.JooqEdition.OSS
  configurations {
    main {
      generateSchemaSourceOnCompilation = true

      generationTool {
        jdbc {
          driver = 'org.postgresql.Driver'
          url = 'jdbc:postgresql://192.168.122.242:5432/feed'
          user = 'feed'
          password = 'feed'
        }
        ...
      }
      ...
    }
    ...
  }
  ...
}
```
3. Build the project
```shell
./gradlew build
```
4. ***[Just in case]*** Above command should create needed classes automatically. but if you have some issues, you can run these commands:
```shell
./gradlew cleanGenerateJooq
./gradlew generateJooq
```

5. Create docker images and run them
```shell
docker build -t simple-feed-scraper-1.0.jar . 
docker-compose up -d
```


## Scripts
**To Generate JOOQ classes based on database schema**
```shell
./gradlew generateJooq
```
**To Clean JOOQ classes**
```shell
./gradlew cleanGenerateJooq
```

**To Compile**
```shell
./gradlew compileJava
```
**To Build**
```shell
./gradlew build
```
**To Unit Test**
```shell
./gradlew test
```
**To Integration Test**
```shell
./gradlew integrationTest
```


