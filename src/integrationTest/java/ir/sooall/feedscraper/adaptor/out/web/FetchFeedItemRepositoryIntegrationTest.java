package ir.sooall.feedscraper.adaptor.out.web;

import ir.sooall.feedscraper.common.RetryNumber;
import ir.sooall.feedscraper.domain.repository.FetchFeedItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class FetchFeedItemRepositoryIntegrationTest {

    private FetchFeedItemRepository fetchFeedItemRepository;
    private FetchFeedItemRepository fetchFeedItemRepositoryWithBackOffImpl;
    private RetryNumber retryNumber = new RetryNumber(3);

    @BeforeEach
    void initUseCase() {
        fetchFeedItemRepository = new FetchFeedItemRepositoryImpl();
        fetchFeedItemRepositoryWithBackOffImpl = new FetchFeedItemRepositoryWithBackOffImpl(retryNumber, fetchFeedItemRepository);
    }

    @Test
    void givenUrl_fetchLastUpdate() {
        String url = "https://www.asriran.com/fa/rss/1/5";
        var response = fetchFeedItemRepositoryWithBackOffImpl.fetchLastUpdate(url, null);
        assertThat(response.getUri(), equalTo(url));
        assertThat(response, notNullValue());
    }


}
