package ir.sooall.feedscraper.config;

import ir.sooall.feedscraper.adaptor.out.db.*;
import ir.sooall.feedscraper.adaptor.out.web.FetchFeedItemRepositoryImpl;
import ir.sooall.feedscraper.adaptor.out.web.FetchFeedItemRepositoryWithBackOffImpl;
import ir.sooall.feedscraper.common.RetryNumber;
import ir.sooall.feedscraper.domain.repository.*;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Autowired
    private DefaultDSLContext dsl;

    @Value("${feeditem.fetch.retry.number}")
    private Integer retryNumber;

    @Bean
    public BookmarkRepository bookmarkRepository() {
        return new BookmarkRepositoryImpl(dsl);
    }

    @Bean
    public FeedItemRepository feedItemRepository() {
        return new FeedItemRepositoryImpl(dsl);
    }

    @Bean
    public FeedRepository feedRepository() {
        return new FeedRepositoryImpl(dsl);
    }

    @Bean
    public FeedSubscriptionRepository feedSubscriptionRepository() {
        return new FeedSubscriptionRepositoryImpl(dsl);
    }

    @Bean
    public UpdatingStatusRepository updatingStatusRepository() {
        return new UpdatingStatusRepositoryImpl(dsl);
    }

    public FetchFeedItemRepository fetchFeedItemRepository() {
        return new FetchFeedItemRepositoryImpl();
    }

    @Bean
    public FetchFeedItemRepository fetchFeedItemRepositoryWithBackOffImpl() {
        return new FetchFeedItemRepositoryWithBackOffImpl(new RetryNumber(retryNumber), fetchFeedItemRepository());
    }

}
