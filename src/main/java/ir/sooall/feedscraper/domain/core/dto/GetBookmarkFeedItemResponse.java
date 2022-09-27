package ir.sooall.feedscraper.domain.core.dto;

import io.vavr.Tuple2;
import ir.sooall.feedscraper.domain.core.entity.FeedItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookmarkFeedItemResponse {
    private List<Tuple2<FeedItem, LocalDateTime>> items;
}
