package ir.sooall.feedscraper.domain.core.dto;

import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FetchFeedItemsRequest {
    private UserId userId;
    private FeedId feedId;
    private LocalDateTime toDate;
    private Long fetchSize;
}
