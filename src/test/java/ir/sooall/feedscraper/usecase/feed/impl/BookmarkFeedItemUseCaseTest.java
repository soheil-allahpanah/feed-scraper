package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedItemBookmarkInfo;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.BookmarkRepository;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.BookmarkFeedItemUseCase;
import ir.sooall.feedscraper.usecase.feed.impl.BookmarkFeedItemUseCasesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

public class BookmarkFeedItemUseCaseTest {

    private final FeedItemRepository feedItemRepository = Mockito.mock(FeedItemRepository.class);
    private final FeedSubscriptionRepository feedSubscriptionRepository = Mockito.mock(FeedSubscriptionRepository.class);
    private final BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepository.class);
    private BookmarkFeedItemUseCase bookmarkFeedItemUseCase;

    private FeedItemId feedItemId;
    private BookmarkFeedItemRequest request;

    @BeforeEach
    void initUseCase() {
        bookmarkFeedItemUseCase = new BookmarkFeedItemUseCasesImpl(feedItemRepository, bookmarkRepository, feedSubscriptionRepository);
        UserId userId = new UserId(1L);
        FeedId feedId = new FeedId(1L);
        feedItemId = new FeedItemId(1L);
        request = new BookmarkFeedItemRequest(userId, feedId, feedItemId);
    }

    @Test
    void bookmarkNotYetBookmarkedFeedItemWouldCallInsertBookmark() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(true).when(feedItemRepository).feedItemExist(request.getFeedId(), request.getFeedItemId());
        doReturn(false).when(bookmarkRepository).checkBookmarkExist(request.getUserId(), request.getFeedItemId());
        doNothing().when(bookmarkRepository).insertBookmark(any(FeedItemBookmarkInfo.class));
        var response = bookmarkFeedItemUseCase.bookmark(request);
        verify((bookmarkRepository), times(1)).insertBookmark(any(FeedItemBookmarkInfo.class));
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(feedItemId, equalTo(response.get().getFeedItemId()));
    }

    @Test
    void bookmarkBookmarkedFeedItemWouldCallUpdateBookmark() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(true).when(feedItemRepository).feedItemExist(request.getFeedId(), request.getFeedItemId());
        doReturn(true).when(bookmarkRepository).checkBookmarkExist(request.getUserId(), request.getFeedItemId());
        doNothing().when(bookmarkRepository).updateBookmark(any(FeedItemBookmarkInfo.class));
        var response = bookmarkFeedItemUseCase.bookmark(request);
        verify((bookmarkRepository), times(1)).updateBookmark(any(FeedItemBookmarkInfo.class));
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(feedItemId, equalTo(response.get().getFeedItemId()));
    }

    @Test
    void bookmarkNotSubscribedFeedWouldReturnEntityNotFoundException() {
        doReturn(false).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        var response = bookmarkFeedItemUseCase.bookmark(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
    }

    @Test
    void bookmarkNotExistedFeedItemWouldReturnEntityNotFoundException() {
        doReturn(true).when(feedSubscriptionRepository).checkSubscription(request.getUserId(), request.getFeedId());
        doReturn(false).when(feedItemRepository).feedItemExist(request.getFeedId(), request.getFeedItemId());
        var response = bookmarkFeedItemUseCase.bookmark(request);
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
    }
}
