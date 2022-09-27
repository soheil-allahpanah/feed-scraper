package ir.sooall.feedscraper.domain.core.dto;

import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class BookmarkFeedItemRequest {
    private UserId userId;
    private FeedId feedId;
    private FeedItemId feedItemId;
}
