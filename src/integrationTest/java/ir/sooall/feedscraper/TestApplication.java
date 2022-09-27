package ir.sooall.feedscraper;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.sooall.feedscraper.adaptor.in.controller.dto.*;
import ir.sooall.feedscraper.common.gson.LocalDateTimeDeserializer;
import ir.sooall.feedscraper.common.gson.LocalDateTimeSerializer;
import ir.sooall.feedscraper.domain.core.dto.RequestStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:schema.sql")
@ContextConfiguration(initializers = {TestApplication.Initializer.class})
@AutoConfigureMockMvc
@Testcontainers
public class TestApplication {

    @Autowired
    private MockMvc mvc;

    private static Gson gson;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa");

    static class Initializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    static void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    @Test
    public void givenUserAndUrl_whenSubscribe_thenReturnJsonArray()
        throws Exception {
        var subscribeFeedReqDto = new SubscribeFeedReqDto(1L, "https://www.asriran.com/fa/rss/1/5");
        var subscribeResult = mvc.perform(post("/feed/subscribe", 42L)
                .contentType("application/json")
                .content(gson.toJson(subscribeFeedReqDto)))
            .andExpect(status().isOk())
            .andReturn();
        var subscribeResponse = gson.fromJson(subscribeResult.getResponse().getContentAsString(), SubscribeFeedResDto.class);
        assertThat(subscribeResponse.getFeedId()).isNotNull();

        var getFeedOfUserResult = mvc.perform(get("/users/1/feeds", 42L)
                .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();
        var getFeedOfUserResponse = gson.fromJson(getFeedOfUserResult.getResponse().getContentAsString(), FeedListResDto.class);
        assertThat(getFeedOfUserResponse.getList()).isNotNull();

        var updateFeedItemsReqDto = new UpdateFeedItemsReqDto(1L, getFeedOfUserResponse.getList().get(0).getId(), null);

        var updateFeedItemsResult = mvc.perform(put("/feed/update", 42L)
                .contentType("application/json")
                .content(gson.toJson(updateFeedItemsReqDto))
            )
            .andExpect(status().isOk())
            .andReturn();
        var updateFeedItemsResponse = gson.fromJson(updateFeedItemsResult.getResponse().getContentAsString(), UpdateFeedItemsResDto.class);
        assertThat(updateFeedItemsResponse.getRequestStatus()).isEqualTo(RequestStatus.SUBMITTED);

    }

}
