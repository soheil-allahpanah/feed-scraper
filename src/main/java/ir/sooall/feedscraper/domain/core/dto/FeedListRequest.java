package ir.sooall.feedscraper.domain.core.dto;

import ir.sooall.feedscraper.domain.core.entity.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedListRequest {
    private UserId userId;
    private Long fetchSize;
    private LocalDateTime toSubscriptionTime;
}
