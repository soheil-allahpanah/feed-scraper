package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsResponse;

public interface FetchFeedItemsUseCase {

    Try<FetchFeedItemsResponse> fetchItem(FetchFeedItemsRequest request);

}
