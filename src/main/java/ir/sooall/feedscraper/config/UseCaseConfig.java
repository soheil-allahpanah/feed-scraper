package ir.sooall.feedscraper.config;

import ir.sooall.feedscraper.domain.repository.*;
import ir.sooall.feedscraper.usecase.feed.*;
import ir.sooall.feedscraper.usecase.feed.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class UseCaseConfig {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private FeedItemRepository feedItemRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedSubscriptionRepository feedSubscriptionRepository;

    @Autowired
    private UpdatingStatusRepository updatingStatusRepository;

    @Autowired
    private FetchFeedItemRepository fetchFeedItemRepository;

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Bean
    public BookmarkFeedItemUseCase bookmarkFeedItemUseCase() {
        return new BookmarkFeedItemUseCasesImpl(feedItemRepository, bookmarkRepository, feedSubscriptionRepository);
    }

    @Bean
    public UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase() {
        return new BookmarkFeedItemUseCasesImpl(feedItemRepository, bookmarkRepository, feedSubscriptionRepository);
    }

    @Bean
    public FetchFeedItemsUseCase fetchFeedItemsUseCase() {
        return new FetchFeedItemsUseCaseImpl(feedItemRepository, updatingStatusRepository, feedSubscriptionRepository);
    }

    @Bean
    public InitiateUpdatingFeedItemsUseCase initiateUpdatingFeedItemsUseCase(UpdateFeedItemUseCase updateFeedItemUseCase) {
        return new InitiateUpdatingFeedItemsUseCaseImpl(feedRepository
            , updatingStatusRepository
            , feedSubscriptionRepository
            , updateFeedItemUseCase);
    }

    @Bean
    public UpdateFeedItemUseCase updateFeedItemUseCase() {
        return new UpdateFeedItemUseCaseImpl(feedRepository
            , fetchFeedItemRepository
            , feedItemRepository
            , updatingStatusRepository
            , threadPoolTaskExecutor
        );
    }

    @Bean
    public SubscribeFeedUseCase subscribeFeedUseCase(UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase) {
        return new FeedSubscriptionUseCasesImpl(feedRepository, feedSubscriptionRepository, unBookmarkFeedItemUseCase);
    }

    @Bean
    public UnSubscribeFeedUseCase unSubscribeFeedUseCase(UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase) {
        return new FeedSubscriptionUseCasesImpl(feedRepository, feedSubscriptionRepository, unBookmarkFeedItemUseCase);
    }

    @Bean
    public FeedQueryUseCases feedQueryUseCases() {
        return new FeedQueryUseCasesImpl(feedRepository, feedSubscriptionRepository);
    }

    @Bean
    public GetBookmarkFeedItemsUseCase getBookmarkFeedItemsUseCase() {
        return new GetBookmarkFeedItemsUseCaseImpl(bookmarkRepository, feedItemRepository);
    }
}
