FROM amazoncorretto:17-alpine3.13-jdk
EXPOSE 8080
ADD build/libs/simple-feed-scraper-1.0.jar simple-feed-scraper.jar
ENTRYPOINT ["java", "-jar","simple-feed-scraper.jar"]
