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

## Config
**In application.properties**
```properties
spring.datasource.url=jdbc:postgresql://192.168.122.242:5432/feed  
spring.datasource.username=feed  
spring.datasource.password=feed  
spring.datasource.driverClassName=org.postgresql.Driver  
jooq.sql.dialect=POSTGRES
## how many time the server would try fetch data from feed provider  
feedItem.fetch.retry.number=3
```
**In build.gradle**
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


