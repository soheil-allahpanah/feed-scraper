package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;

import java.util.Optional;

public interface UpdatingStatusRepository {

    FeedUpdatingStatus insertAndReturnFeedStatus(FeedId feedId);

    Optional<FeedUpdatingStatus> findFeedStatus(FeedId feedId);

    void updateUpdatingStatus(FeedUpdatingStatus updatingStatus);

}
