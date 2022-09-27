package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemResponse;

public interface BookmarkFeedItemUseCase {
    Try<BookmarkFeedItemResponse> bookmark(BookmarkFeedItemRequest request);
}
