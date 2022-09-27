package ir.sooall.feedscraper.domain.core.entity;

import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemRequest;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedItemBookmarkInfo {
    private FeedItemBookmarkInfoId id;
    private FeedItemId feedItemId;
    private UserId userId;
    private Boolean bookmarked;
    private LocalDateTime creationTime;
    private LocalDateTime lastUpdatedTime;

    public static FeedItemBookmarkInfo from(BookmarkFeedItemRequest request) {
        var now = LocalDateTime.now();
        return builder().feedItemId(request.getFeedItemId())
            .userId(request.getUserId())
            .bookmarked(true)
            .lastUpdatedTime(now)
            .build();
    }

    public static FeedItemBookmarkInfo from(UnBookmarkFeedItemRequest request) {
        var now = LocalDateTime.now();
        return builder().feedItemId(request.getFeedItemId())
            .userId(request.getUserId())
            .bookmarked(false)
            .lastUpdatedTime(now)
            .build();
    }

    public void unBookmark() {
        lastUpdatedTime = LocalDateTime.now();
        bookmarked = false;
    }
}
