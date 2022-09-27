FROM amazoncorretto:17-alpine3.13-jdk
EXPOSE 8080
ADD build/libs/feed-scraper-1.0.jar feed-scraper.jar
ENTRYPOINT ["java", "-jar","feed-scraper.jar"]
