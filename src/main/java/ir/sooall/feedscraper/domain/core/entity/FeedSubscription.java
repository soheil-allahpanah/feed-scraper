package ir.sooall.feedscraper.domain.core.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedSubscription {
    private FeedSubscriptionId id;
    private UserId userId;
    private FeedId feedId;
    private Boolean subscribed;
    private LocalDateTime lastUpdatedTime;
    private LocalDateTime creationTime;


    public static FeedSubscription initiate(FeedId feedId, UserId userId) {
        var now = LocalDateTime.now();
        return FeedSubscription.builder()
            .feedId(feedId)
            .userId(userId)
            .subscribed(true)
            .lastUpdatedTime(now)
            .creationTime(now)
            .build();
    }

    public FeedSubscription subscribe() {
        lastUpdatedTime = LocalDateTime.now();
        subscribed = true;
        return this;
    }

    public FeedSubscription unsubscribe() {
        lastUpdatedTime = LocalDateTime.now();
        subscribed = false;
        return this;
    }
}
