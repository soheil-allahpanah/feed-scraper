package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedSubscriptionRepository {

    Boolean checkSubscription(UserId userId, FeedId feedId);

    Optional<FeedSubscription> findSubscription(UserId userId, FeedId feedId);

    Optional<List<FeedSubscription>> findSubscriptions(UserId userId, Long fetchSize, LocalDateTime toSubscriptionTime);

    void insertSubscription(FeedSubscription subscription);

    void updateSubscription(FeedSubscription subscription);
}
