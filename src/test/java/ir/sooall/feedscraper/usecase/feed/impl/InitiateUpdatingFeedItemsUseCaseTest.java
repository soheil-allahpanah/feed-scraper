package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.dto.RequestStatus;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.entity.*;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.domain.repository.UpdatingStatusRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.InitiateUpdatingFeedItemsUseCase;
import ir.sooall.feedscraper.usecase.feed.UpdateFeedItemUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

public class InitiateUpdatingFeedItemsUseCaseTest {
    private final FeedRepository feedRepository = Mockito.mock(FeedRepository.class);
    private final UpdatingStatusRepository updatingStatusRepository = Mockito.mock(UpdatingStatusRepository.class);
    private final FeedSubscriptionRepository feedSubscriptionRepository = Mockito.mock(FeedSubscriptionRepository.class);
    private final UpdateFeedItemUseCase updateFeedItemUseCase = Mockito.mock(UpdateFeedItemUseCase.class);
    private UpdateFeedItemsRequest request;
    private InitiateUpdatingFeedItemsUseCase useCase;
    private Feed feed;
    private FeedUpdatingStatus updatedStatus;
    private FeedUpdatingStatus updatingStatus;

    @BeforeEach
    void initUseCase() {
        useCase = new InitiateUpdatingFeedItemsUseCaseImpl(feedRepository
            , updatingStatusRepository
            , feedSubscriptionRepository
            , updateFeedItemUseCase);

        UserId userId = new UserId(1L);
        FeedId feedId = new FeedId(1L);
        var now = LocalDateTime.now();
        feed = Feed.builder()
            .id(feedId)
            .uri("http://example.com/rss")
            .title("Fake Title")
            .description("Fake Description")
            .encoding("UTF_8")
            .feedType("rss")
            .link("http://example.com/rss/link")
            .webMaster(null)
            .managingEditor(null)
            .docs(null)
            .generator(null)
            .styleSheet(null)
            .links(null)
            .icon(null)
            .image(null)
            .authors(null)
            .contributors(null)
            .lastUpdatedTime(now)
            .creationTime(now)
            .build();
        updatedStatus = FeedUpdatingStatus.builder()
            .id(new FeedUpdatingStatusId(1L))
            .feedId(feedId)
            .status(FeedUpdatingStatusValue.UPDATED)
            .lastFeedItemUri("1111")
            .lastUpdatedTime(now)
            .creationTime(now).build();

        updatingStatus = FeedUpdatingStatus.builder()
            .id(new FeedUpdatingStatusId(1L))
            .feedId(feedId)
            .status(FeedUpdatingStatusValue.UPDATING)
            .lastFeedItemUri("1111")
            .lastUpdatedTime(now)
            .creationTime(now).build();

        request = new UpdateFeedItemsRequest(userId, feedId);
    }

    @Test
    public void updateFeedItemOfGivenUserIdAndGivenFeedIdWhenNoError() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(Optional.of(feed)).when(feedRepository).fetchFeed(request.getFeedId());
        doReturn(Optional.of(updatedStatus)).when(updatingStatusRepository).findFeedStatus(feed.getId());
        doNothing().when(updateFeedItemUseCase).updateItems(feed, Optional.of(updatedStatus));
        var response = useCase.initUpdateFeedItems(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getRequestStatus(), equalTo(RequestStatus.SUBMITTED));
    }

    @Test
    public void updateFeedItemOfGivenUserIdAndGivenFeedIdWhenSubscriptionNotFound() {
        doReturn(false).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        var response = useCase.initUpdateFeedItems(request);
        verify(feedRepository, times(0)).fetchFeed(request.getFeedId());
        verify(updatingStatusRepository, times(0)).findFeedStatus(feed.getId());
        verify(updateFeedItemUseCase, times(0)).updateItems(feed, Optional.of(updatedStatus));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
    }

    @Test
    public void updateFeedItemOfGivenUserIdAndGivenFeedIdWhenFeedNotFound() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(Optional.empty()).when(feedRepository).fetchFeed(request.getFeedId());
        var response = useCase.initUpdateFeedItems(request);
        verify(updatingStatusRepository, times(0)).findFeedStatus(feed.getId());
        verify(updateFeedItemUseCase, times(0)).updateItems(feed, Optional.of(updatedStatus));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
    }

    @Test
    public void updateFeedItemOfGivenUserIdAndGivenFeedIdWhenStatusIsUpdating() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(Optional.of(feed)).when(feedRepository).fetchFeed(request.getFeedId());
        doReturn(Optional.of(updatingStatus)).when(updatingStatusRepository).findFeedStatus(feed.getId());
        var response = useCase.initUpdateFeedItems(request);
        verify(updateFeedItemUseCase, times(0)).updateItems(feed, Optional.of(updatedStatus));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(IllegalStateException.class));
    }
}
