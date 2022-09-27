package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;

public interface UnBookmarkFeedItemUseCase {

    Try<UnBookmarkFeedItemResponse> unBookmark(UnBookmarkFeedItemRequest request);

    void unBookmarkItemsFromUnSubscribedFeed(UserId userId, FeedId feedId);

}
