package ir.sooall.feedscraper.adaptor.in.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class BookmarkFeedItemResDto {
    private Long feedItemId;
    private LocalDateTime lastUpdatedDate;
}
