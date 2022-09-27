package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.Feed;

public interface FetchFeedItemRepository {

    Feed fetchLastUpdate(String uri, String lastFeedItemUri);

}
