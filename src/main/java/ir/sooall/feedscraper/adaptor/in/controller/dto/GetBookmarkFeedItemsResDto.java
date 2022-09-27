package ir.sooall.feedscraper.adaptor.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookmarkFeedItemsResDto {
    private List<FeedItemDto> items;
}
