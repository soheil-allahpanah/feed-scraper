package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedItem;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedItemRepository {
    Boolean feedItemExist(FeedId feedId, FeedItemId feedItemId);

    Optional<FeedItem> fetchFeedItem(FeedItemId feedItemId);

    List<FeedItem> fetchFeedItems(FeedId feedId, Long fetchSize, LocalDateTime fromContentUpdatedDate);

    void updateFeedItems(List<FeedItem> entries);
}
