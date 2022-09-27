package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import ir.sooall.feedscraper.domain.repository.*;
import ir.sooall.feedscraper.usecase.feed.UpdateFeedItemUseCase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class UpdateFeedItemUseCaseImpl implements UpdateFeedItemUseCase {

    private final FeedRepository feedRepository;
    private final FetchFeedItemRepository fetchFeedItemsRepository;
    private final FeedItemRepository feedItemRepository;
    private final UpdatingStatusRepository updatingStatusRepository;
    private final Executor threadPoolTaskExecutor;

    public UpdateFeedItemUseCaseImpl(FeedRepository feedRepository
        , FetchFeedItemRepository fetchFeedItemsRepository
        , FeedItemRepository feedItemRepository
        , UpdatingStatusRepository updatingStatusRepository
        , Executor threadPoolTaskExecutor) {
        this.feedRepository = feedRepository;
        this.fetchFeedItemsRepository = fetchFeedItemsRepository;
        this.feedItemRepository = feedItemRepository;
        this.updatingStatusRepository = updatingStatusRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Transactional
    @Override
    public void updateItems(Feed feed, Optional<FeedUpdatingStatus> updatingStatusOptional) {
        FeedUpdatingStatus updatingStatus = updatingStatusOptional.orElseGet(() -> updatingStatusRepository.insertAndReturnFeedStatus(feed.getId()));
        updatingStatus.changeStatusToUpdating();
        updatingStatusRepository.updateUpdatingStatus(updatingStatus);
        CompletableFuture
            .supplyAsync(() -> updateFeed(feed, updatingStatus.getLastFeedItemUri()), threadPoolTaskExecutor)
            .thenApplyAsync(this::updateFeedItem, threadPoolTaskExecutor)
            .thenAcceptAsync((fetchedFeed) -> updateFeedStatus(fetchedFeed, updatingStatus), threadPoolTaskExecutor)
            .handle((unused, throwable) -> {
                if (throwable != null) {
                    updatingStatus.changeStatusToError(feed);
                    updatingStatusRepository.updateUpdatingStatus(updatingStatus);
                }
                return unused;
            });
    }

    private Feed updateFeed(Feed feed, String lastFeedItemUri) {
        var fetchedFeed = fetchFeedItemsRepository.fetchLastUpdate(feed.getUri(), lastFeedItemUri);
        fetchedFeed.setId(feed.getId());
        fetchedFeed.setCreationTime(feed.getCreationTime());
        feedRepository.updateFeed(fetchedFeed);
        return fetchedFeed;
    }

    private Feed updateFeedItem(Feed feed) {
        feed.getEntries().forEach(e -> {
            var now = LocalDateTime.now();
            e.setCreationTime(now);
            e.setLastUpdatedTime(now);
            e.setSource(feed.getId());
        });
        feedItemRepository.updateFeedItems(feed.getEntries());
        return feed;
    }

    private void updateFeedStatus(Feed feed, FeedUpdatingStatus status) {
        status.changeStatusToUpdated(feed);
        updatingStatusRepository.updateUpdatingStatus(status);
    }
}
