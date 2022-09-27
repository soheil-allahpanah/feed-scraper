package ir.sooall.feedscraper.domain.core.entity;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedUpdatingStatus {
    private FeedUpdatingStatusId id;
    private FeedId feedId;
    private FeedUpdatingStatusValue status;
    private String lastFeedItemUri;
    private LocalDateTime lastUpdatedTime;
    private LocalDateTime creationTime;

    public void changeStatusToUpdating() {
        status = FeedUpdatingStatusValue.UPDATING;
        lastUpdatedTime = LocalDateTime.now();
    }

    public void changeStatusToUpdated(Feed feed) {
        lastFeedItemUri = !feed.getEntries().isEmpty() ? feed.getEntries().get(0).getUri() : lastFeedItemUri;
        status = FeedUpdatingStatusValue.UPDATED;
        lastUpdatedTime = LocalDateTime.now();
    }
    public void changeStatusToError(Feed feed) {
        lastFeedItemUri = !feed.getEntries().isEmpty() ? feed.getEntries().get(0).getUri() : lastFeedItemUri;
        status = FeedUpdatingStatusValue.ERROR;
        lastUpdatedTime = LocalDateTime.now();
    }
}
