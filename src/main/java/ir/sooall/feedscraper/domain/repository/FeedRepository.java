package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;

import java.util.Optional;


public interface FeedRepository {

    Optional<Feed> fetchFeed(FeedId feedId);

    Boolean checkFeedExist(FeedId feedId);

    Optional<FeedId> findFeedIdByURI(String url);

    FeedId initiateFeed(String url);

    void updateFeed(Feed localFeed);
}
