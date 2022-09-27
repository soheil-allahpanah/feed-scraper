package ir.sooall.feedscraper.usecase.feed.impl;

import com.rometools.utils.Lists;
import ir.sooall.feedscraper.domain.core.dto.FeedListRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedRequest;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.FeedQueryUseCases;
import ir.sooall.feedscraper.usecase.feed.impl.FeedQueryUseCasesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedQueryUseCasesTest {
    private final FeedRepository feedRepository = Mockito.mock(FeedRepository.class);
    private final FeedSubscriptionRepository feedSubscriptionRepository = Mockito.mock(FeedSubscriptionRepository.class);
    private FeedQueryUseCases feedQueryUseCases;
    private Feed feed;
    private UserId userId;
    private FeedId feedId;
    private FeedSubscription feedSubscriptionWithSubscribedStatus;
    private FeedSubscription feedSubscriptionWithUnSubscribedStatus;

    @BeforeEach
    void initUseCase() {
        userId = new UserId(1L);
        feedId = new FeedId(1L);
        feedQueryUseCases = new FeedQueryUseCasesImpl(feedRepository, feedSubscriptionRepository);
        var now = LocalDateTime.now();
        feedSubscriptionWithSubscribedStatus = FeedSubscription.builder()
            .feedId(feedId)
            .userId(userId)
            .subscribed(true)
            .lastUpdatedTime(now)
            .creationTime(now)
            .build();
        feedSubscriptionWithUnSubscribedStatus = FeedSubscription.builder()
            .feedId(feedId)
            .userId(userId)
            .subscribed(false)
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
    @DisplayName("Tests for the method getUserFeed")
    class GetUserFeed {
        private List<FeedSubscription> feedSubscriptions;
        private FeedListRequest request;

        @BeforeEach
        void initUseCase() {
            var now = LocalDateTime.now();
            feedSubscriptions = Lists.create(feedSubscriptionWithSubscribedStatus);
            request = new FeedListRequest(userId, 10L, now);
        }

        @Test
        void getUserFeedWhenEveryThingIsOk() {
            doReturn(Optional.of(feedSubscriptions)).when(feedSubscriptionRepository).findSubscriptions(request.getUserId()
                , request.getFetchSize()
                , request.getToSubscriptionTime());
            var feedId = feedSubscriptions.get(0).getFeedId();
            doReturn(Optional.of(feed)).when(feedRepository).fetchFeed(feedId);
            var response = feedQueryUseCases.getUserFeed(request);
            assertThat(response.isSuccess(), equalTo(true));
        }

        @Test
        void getUserFeedWhenGivenUserIdDoseNotHaveAnySubscriptions() {
            doReturn(Optional.empty()).when(feedSubscriptionRepository).findSubscriptions(request.getUserId()
                , request.getFetchSize()
                , request.getToSubscriptionTime());
            var response = feedQueryUseCases.getUserFeed(request);
            verify((feedRepository), times(0)).fetchFeed(any(FeedId.class));
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        }
    }

    @Nested
    @DisplayName("Tests for the method getFeed")
    class GetFeed {
        private FeedRequest request;

        @BeforeEach
        void initUseCase() {
            request = new FeedRequest(feedId, userId);
        }

        @Test
        void getFeedWhenEveryThingIsOk() {
            doReturn(Optional.of(feedSubscriptionWithSubscribedStatus)).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            doReturn(Optional.of(feed)).when(feedRepository).fetchFeed(feedSubscriptionWithSubscribedStatus.getFeedId());
            var response = feedQueryUseCases.getFeed(request);
            assertThat(response.isSuccess(), equalTo(true));
        }

        @Test
        void getFeedWhenForGivenUserIdAndFeedIdNoSubscriptionExist() {
            doReturn(Optional.empty()).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            var response = feedQueryUseCases.getFeed(request);
            verify((feedRepository), times(0)).fetchFeed(any(FeedId.class));
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        }

        @Test
        void getFeedWhenForGivenUserIdAndFeedIdSubscriptionUnSubscribedBefore() {
            doReturn(Optional.of(feedSubscriptionWithUnSubscribedStatus)).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            var response = feedQueryUseCases.getFeed(request);
            verify((feedRepository), times(0)).fetchFeed(any(FeedId.class));
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
        }

        @Test
        void getFeedWhenUserSubscribeGivenFeedIdButCorrespondFeedNotExist() {
            doReturn(Optional.of(feedSubscriptionWithSubscribedStatus)).when(feedSubscriptionRepository).findSubscription(request.getUserId()
                , request.getFeedId());
            doReturn(Optional.empty()).when(feedRepository).fetchFeed(feedSubscriptionWithSubscribedStatus.getFeedId());
            var response = feedQueryUseCases.getFeed(request);
            assertThat(response.isFailure(), equalTo(true));
            assertThat(response.getCause(), instanceOf(IllegalStateException.class));
        }
    }


}
