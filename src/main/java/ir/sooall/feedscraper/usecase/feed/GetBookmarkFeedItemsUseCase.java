package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.GetBookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.dto.GetBookmarkFeedItemsRequest;

public interface GetBookmarkFeedItemsUseCase {
    Try<GetBookmarkFeedItemResponse> getBookmarkedItems(GetBookmarkFeedItemsRequest request);
}
