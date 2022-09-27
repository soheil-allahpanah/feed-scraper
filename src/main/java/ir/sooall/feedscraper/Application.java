package ir.sooall.feedscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {
    public static final String DATE_RESULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
