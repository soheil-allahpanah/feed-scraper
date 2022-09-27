package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.SubscribeFeedUseCase;
import ir.sooall.feedscraper.usecase.feed.UnBookmarkFeedItemUseCase;
import ir.sooall.feedscraper.usecase.feed.UnSubscribeFeedUseCase;
import ir.sooall.feedscraper.usecase.feed.impl.FeedSubscriptionUseCasesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedSubscriptionUseCasesTest {

    private final FeedRepository feedRepository = Mockito.mock(FeedRepository.class);
    private final FeedSubscriptionRepository feedSubscriptionRepository = Mockito.mock(FeedSubscriptionRepository.class);
    private UserId userId;
    private FeedId feedId;
    private Feed feed;
    private FeedSubscription feedSubscription;

    @BeforeEach
    void initUseCase() {
        userId = new UserId(1L);
        feedId = new FeedId(1L);
        var now = LocalDateTime.now();
        feedSubscription = FeedSubscription.builder()
            .feedId(feedId)
            .userId(userId)
            .subscribed(true)
            .lastUpdatedTime(now)
            .creationTime(now)
            .build();

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
    }

    @Nested
    @DisplayName("Tests for the method subscribe")
    class Subscribe {
        private SubscribeFeedUseCase subscribeFeedUseCase = Mockito.mock(SubscribeFeedUseCase.class);
        private UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase = Mockito.mock(UnBookmarkFeedItemUseCase.class);
        private SubscribeFeedRequest request;

        @BeforeEach
        void initUseCase() {
            subscribeFeedUseCase = new FeedSubscriptionUseCasesImpl(feedRepository, feedSubscriptionRepository, unBookmarkFeedItemUseCase);
            var url = "http://example.com/rss";
            request = new SubscribeFeedRequest(userId, url);
        }

        @Test
        void givenUserSubscribeGivenUrlFromScratch() {
            doReturn(Optional.empty()).when(feedRepository).findFeedIdByURI(request.getUrl());
            doReturn(feedId).when(feedRepository).initiateFeed(request.getUrl());
            doReturn(Optional.empty()).when(feedSubscriptionRepository).findSubscription(request.getUserId(), feedId);
            doNothing().when(feedSubscriptionRepository).insertSubscription(any(FeedSubscription.class));
            var response = subscribeFeedUseCase.subscribe(request);
            verify((feedSubscriptionRepository), times(1)).insertSubscription(any(FeedSubscription.class));
            assertThat(response.isSuccess(), equalTo(true));
        }

        @Test
        void givenUserSubscribeGivenUrlWhenFeedAlreadyExist() {
            doReturn(Optional.of(feedId)).when(feedRepository).findFeedIdByURI(request.getUrl());
            doReturn(Optional.empty()).when(feedSubscriptionRepository).findSubscription(request.getUserId(), feedId);
            doNothing().when(feedSubscriptionRepository).insertSubscription(any(FeedSubscription.class));
            var response = subscribeFeedUseCase.subscribe(request);
            verify((feedRepository), times(0)).initiateFeed(request.getUrl());
            verify((feedSubscriptionRepository), times(1)).insertSubscription(any(FeedSubscription.class));
            assertThat(response.isSuccess(), equalTo(true));
        }

        @Test
        void givenUserSubscribeGivenUrlWhenFeedSubscriptionExist() {
            doReturn(Optional.of(feedId)).when(feedRepository).findFeedIdByURI(request.getUrl());
            doReturn(Optional.of(feedSubscription)).when(feedSubscriptionRepository).findSubscription(request.getUserId(), feedId);
            doNothing().when(feedSubscriptionRepository).updateSubscription(any(FeedSubscription.class));
            var response = subscribeFeedUseCase.subscribe(request);
            verify((feedRepository), times(0)).initiateFeed(request.getUrl());
            verify((feedSubscriptionRepository), times(1)).updateSubscription(any(FeedSubscription.class));
            assertThat(response.isSuccess(), equalTo(true));
        }

    }

    @Nested
    @DisplayName("Tests for the method nnSubscribe")
    class UnSubscribe {
        private UnSubscribeFeedUseCase unSubscribeFeedUseCase = Mockito.mock(UnSubscribeFeedUseCase.class);
        private UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase = Mockito.mock(UnBookmarkFeedItemUseCase.class);
        private UnSubscribeFeedRequest request;

        @BeforeEach
        void initUseCase() {
            unSubscribeFeedUseCase = new FeedSubscriptionUseCasesImpl(feedRepository, feedSubscriptionRepository, unBookmarkFeedItemUseCase);
            request = new UnSubscribeFeedRequest(userId, feedId);
        }

        @Test
        void unSubscribeFeedWhenEveryThingIsOk() {
            doReturn(Optional.of(feedSubscription)).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            doReturn(true).when(feedRepository).checkFeedExist(request.getFeedId());
            doNothing().when(unBookmarkFeedItemUseCase).unBookmarkItemsFromUnSubscribedFeed(request.getUserId(), request.getFeedId());
            doNothing().when(feedSubscriptionRepository).updateSubscription(feedSubscription.unsubscribe());
            var response = unSubscribeFeedUseCase.unSubscribe(request);
            verify((feedSubscriptionRepository), times(1)).updateSubscription(any(FeedSubscription.class));
            assertThat(response.isSuccess(), equalTo(true));
        }

        @Test
        void unSubscribeFeedSubscriptionNotExist() {
            doReturn(Optional.empty()).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            var response = unSubscribeFeedUseCase.unSubscribe(request);
            verify((feedRepository), times(0)).checkFeedExist(request.getFeedId());
            verify((unBookmarkFeedItemUseCase), times(0)).unBookmarkItemsFromUnSubscribedFeed(request.getUserId(), request.getFeedId());
            verify((feedSubscriptionRepository), times(0)).updateSubscription(any(FeedSubscription.class));
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        }

        @Test
        void unSubscribeFeedWhenFeedNotFound() {
            doReturn(Optional.of(feedSubscription)).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            doReturn(false).when(feedRepository).checkFeedExist(request.getFeedId());
            var response = unSubscribeFeedUseCase.unSubscribe(request);
            verify((unBookmarkFeedItemUseCase), times(0)).unBookmarkItemsFromUnSubscribedFeed(request.getUserId(), request.getFeedId());
            verify((feedSubscriptionRepository), times(0)).updateSubscription(any(FeedSubscription.class));
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        }
    }
}
