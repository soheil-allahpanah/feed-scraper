package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatusValue;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.domain.repository.UpdatingStatusRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.FetchFeedItemsUseCase;
import ir.sooall.feedscraper.usecase.feed.impl.FetchFeedItemsUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

public class FetchFeedItemsUseCaseTest {

    private final FeedItemRepository feedItemRepository = Mockito.mock(FeedItemRepository.class);
    private final UpdatingStatusRepository updatingStatusRepository = Mockito.mock(UpdatingStatusRepository.class);
    private final FeedSubscriptionRepository feedSubscriptionRepository = Mockito.mock(FeedSubscriptionRepository.class);
    private FetchFeedItemsRequest request;
    private FetchFeedItemsUseCase fetchFeedItemsUseCase;
    private FeedUpdatingStatus status;

    @BeforeEach
    void initUseCase() {
        fetchFeedItemsUseCase = new FetchFeedItemsUseCaseImpl(feedItemRepository, updatingStatusRepository, feedSubscriptionRepository);
        UserId userId = new UserId(1L);
        FeedId feedId = new FeedId(1L);
        var now = LocalDateTime.now();
        request = new FetchFeedItemsRequest(userId, feedId, now, 10L);
        status = FeedUpdatingStatus.builder()
            .feedId(feedId)
            .status(FeedUpdatingStatusValue.UPDATED)
            .lastFeedItemUri("1234567890")
            .lastUpdatedTime(now)
            .creationTime(now)
            .build();
    }

    @Test
    void fetchFeedItemsWhenEverythingIsOK() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(Optional.of(status)).when(updatingStatusRepository).findFeedStatus(request.getFeedId());
        var response = fetchFeedItemsUseCase.fetchItem(request);
        assertThat(response.isSuccess(), equalTo(true));
    }

    @Test
    void fetchFeedItemsWhenNoSubscriptionExist() {
        doReturn(false).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        var response = fetchFeedItemsUseCase.fetchItem(request);
        verify((updatingStatusRepository), times(0)).findFeedStatus(any(FeedId.class));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        assertThat(response.isFailure(), equalTo(true));
    }

    @Test
    void fetchFeedItemsWhenNoFeedStatusExist() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(Optional.empty()).when(updatingStatusRepository).findFeedStatus(request.getFeedId());
        var response = fetchFeedItemsUseCase.fetchItem(request);
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        assertThat(response.isFailure(), equalTo(true));
    }
}
