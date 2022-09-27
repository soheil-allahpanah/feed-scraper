package ir.sooall.feedscraper.adaptor.in.controller.dto;

import ir.sooall.feedscraper.domain.core.dto.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateFeedItemsResDto {
    private RequestStatus requestStatus;
    private Long feedId;
}
