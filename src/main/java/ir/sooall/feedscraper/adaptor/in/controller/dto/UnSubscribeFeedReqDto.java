package ir.sooall.feedscraper.adaptor.in.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UnSubscribeFeedReqDto {
    private @NotNull Long userId;
    private @NotNull Long feedId;
}
