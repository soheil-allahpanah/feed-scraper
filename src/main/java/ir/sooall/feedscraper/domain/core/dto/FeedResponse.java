package ir.sooall.feedscraper.domain.core.dto;

import ir.sooall.feedscraper.domain.core.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {
    private Feed feed;
}
