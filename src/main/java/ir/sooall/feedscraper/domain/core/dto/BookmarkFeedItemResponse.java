package ir.sooall.feedscraper.domain.core.dto;

import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class BookmarkFeedItemResponse {
    private FeedItemId feedItemId;
    private LocalDateTime lastUpdatedDate;
}
