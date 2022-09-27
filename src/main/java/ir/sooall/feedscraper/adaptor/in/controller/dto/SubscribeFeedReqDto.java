package ir.sooall.feedscraper.adaptor.in.controller.dto;

import ir.sooall.feedscraper.common.validator.Uri;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class SubscribeFeedReqDto {
    private @NotNull Long userId;
    private @NotBlank @Uri String url;
}
