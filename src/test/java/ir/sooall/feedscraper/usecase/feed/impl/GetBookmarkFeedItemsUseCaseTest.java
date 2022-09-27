package ir.sooall.feedscraper.usecase.feed.impl;

import ir.sooall.feedscraper.domain.core.dto.GetBookmarkFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.entity.*;
import ir.sooall.feedscraper.domain.repository.BookmarkRepository;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.GetBookmarkFeedItemsUseCase;
import ir.sooall.feedscraper.usecase.feed.impl.GetBookmarkFeedItemsUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;


public class GetBookmarkFeedItemsUseCaseTest {

    private GetBookmarkFeedItemsUseCase useCase;
    private final BookmarkRepository bookmarkRepository = Mockito.mock(BookmarkRepository.class);
    private final FeedItemRepository feedItemRepository = Mockito.mock(FeedItemRepository.class);
    private GetBookmarkFeedItemsRequest request;
    private List<FeedItemBookmarkInfo> bookMarkedItems;
    private List<FeedItem> feedItems;

    @BeforeEach
    void initUseCase() {
        useCase = new GetBookmarkFeedItemsUseCaseImpl(bookmarkRepository, feedItemRepository);
        UserId userId = new UserId(1L);
        request = new GetBookmarkFeedItemsRequest(userId, null, null);
        bookMarkedItems = new ArrayList<>();
        feedItems = new ArrayList<>();
        bookMarkedItems.add(FeedItemBookmarkInfo
            .builder()
            .id(new FeedItemBookmarkInfoId(1L))
            .feedItemId(new FeedItemId(1L))
            .userId(userId)
            .bookmarked(true)
            .creationTime(LocalDateTime.now())
            .lastUpdatedTime(LocalDateTime.now()).build()
        );
        bookMarkedItems.add(FeedItemBookmarkInfo
            .builder()
            .id(new FeedItemBookmarkInfoId(2L))
            .feedItemId(new FeedItemId(2L))
            .userId(userId)
            .bookmarked(true)
            .creationTime(LocalDateTime.now())
            .lastUpdatedTime(LocalDateTime.now()).build()
        );
        bookMarkedItems.add(FeedItemBookmarkInfo
            .builder()
            .id(new FeedItemBookmarkInfoId(3L))
            .feedItemId(new FeedItemId(3L))
            .userId(userId)
            .bookmarked(true)
            .creationTime(LocalDateTime.now())
            .lastUpdatedTime(LocalDateTime.now()).build()
        );
        feedItems.add(FeedItem
            .builder().id(new FeedItemId(1L)).source(new FeedId(1L))
            .uri("1").link(null).comments("Fake Comment").contentUpdatedDate(LocalDateTime.now()).title("Fake Title")
            .description(null).links(null).contents(null).enclosures(null).authors(null).contributors(null)
            .lastUpdatedTime(LocalDateTime.now()).creationTime(LocalDateTime.now()).build()
        );
        feedItems.add(FeedItem
            .builder().id(new FeedItemId(2L)).source(new FeedId(1L))
            .uri("1").link(null).comments("Fake Comment").contentUpdatedDate(LocalDateTime.now()).title("Fake Title")
            .description(null).links(null).contents(null).enclosures(null).authors(null).contributors(null)
            .lastUpdatedTime(LocalDateTime.now()).creationTime(LocalDateTime.now()).build()
        );
        feedItems.add(FeedItem
            .builder().id(new FeedItemId(3L)).source(new FeedId(2L))
            .uri("1").link(null).comments("Fake Comment").contentUpdatedDate(LocalDateTime.now()).title("Fake Title")
            .description(null).links(null).contents(null).enclosures(null).authors(null).contributors(null)
            .lastUpdatedTime(LocalDateTime.now()).creationTime(LocalDateTime.now()).build()
        );
    }

    @Test
    public void getBookmarkedItemsWhenEveryThingIsOk() {
        doReturn(Optional.of(bookMarkedItems)).when(bookmarkRepository).findBookmarkedItems(request.getUserId(), request.getFetchSize(), request.getToBookmarkTime());
        for (int i = 0; i < bookMarkedItems.size(); i++) {
            doReturn(Optional.of(feedItems.get(i))).when(feedItemRepository).fetchFeedItem(bookMarkedItems.get(i).getFeedItemId());
        }
        var response = useCase.getBookmarkedItems(request);
        assertThat(response.get().getItems().size(), equalTo(bookMarkedItems.size()));
        assertThat(response.isSuccess(), equalTo(true));
        verify(feedItemRepository, times(bookMarkedItems.size())).fetchFeedItem(any(FeedItemId.class));
    }

    @Test
    public void getBookmarkedItemsWhenSomeFeedItemNotFound() {
        doReturn(Optional.of(bookMarkedItems)).when(bookmarkRepository).findBookmarkedItems(request.getUserId(), request.getFetchSize(), request.getToBookmarkTime());
        for (int i = 0; i < bookMarkedItems.size(); i++) {
            if (i % 2 == 0) {
                doReturn(Optional.of(feedItems.get(i))).when(feedItemRepository).fetchFeedItem(bookMarkedItems.get(i).getFeedItemId());
            } else {
                doReturn(Optional.empty()).when(feedItemRepository).fetchFeedItem(bookMarkedItems.get(i).getFeedItemId());
            }
        }
        var response = useCase.getBookmarkedItems(request);
        assertThat(response.isSuccess(), equalTo(true));
        assertThat(response.get().getItems().size(), lessThan(bookMarkedItems.size()));
        verify(feedItemRepository, times(bookMarkedItems.size())).fetchFeedItem(any(FeedItemId.class));
    }

    @Test
    public void getBookmarkedItemsWhenNoFeedItemBookmarked() {
        doReturn(Optional.empty()).when(bookmarkRepository).findBookmarkedItems(request.getUserId(), request.getFetchSize(), request.getToBookmarkTime());
        var response = useCase.getBookmarkedItems(request);
        verify(feedItemRepository, times(0)).fetchFeedItem(any(FeedItemId.class));
        assertThat(response.isFailure(), equalTo(true));
        assertThat(response.getCause(), instanceOf(EntityNotFoundException.class));
    }
}

