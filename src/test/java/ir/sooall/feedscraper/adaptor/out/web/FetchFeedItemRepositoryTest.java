package ir.sooall.feedscraper.adaptor.out.web;

import ir.sooall.feedscraper.adaptor.out.web.FetchFeedItemRepositoryWithBackOffImpl;
import ir.sooall.feedscraper.common.RetryNumber;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.repository.FetchFeedItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

public class FetchFeedItemRepositoryTest {

    private FetchFeedItemRepository fetchFeedItemRepository = Mockito.mock(FetchFeedItemRepository.class) ;
    private FetchFeedItemRepository fetchFeedItemRepositoryWithBackOffImpl ;
    private RetryNumber retryNumber = new RetryNumber(3);
    private Feed feed;

    @BeforeEach
    void initUseCase() {
        fetchFeedItemRepositoryWithBackOffImpl = new FetchFeedItemRepositoryWithBackOffImpl(retryNumber, fetchFeedItemRepository);
        var feedId = new FeedId(1L);
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
            .build(); }

    @Test
    void givenUrl_fetchLastUpdate_WhenEveryThingIsOk() {
        String url  = "https://www.asriran.com/fa/rss/1/5";
        doReturn(feed).when(fetchFeedItemRepository).fetchLastUpdate(url, null);
        var response = fetchFeedItemRepositoryWithBackOffImpl.fetchLastUpdate(url, null);
        verify((fetchFeedItemRepository), times(1)).fetchLastUpdate(url, null);
        assertThat(response, equalTo(feed));
    }

    @Test
    void givenUrl_fetchLastUpdate_WhenFetchingFeedGotException() {
        String url  = "https://www.asriran.com/fa/rss/1/5";
        doThrow(IllegalStateException.class).when(fetchFeedItemRepository).fetchLastUpdate(url, null);
        try{
            fetchFeedItemRepositoryWithBackOffImpl.fetchLastUpdate(url, null);
        } catch (IllegalStateException e) {
            verify((fetchFeedItemRepository), times(retryNumber.value())).fetchLastUpdate(url, null);

        }
    }
}
