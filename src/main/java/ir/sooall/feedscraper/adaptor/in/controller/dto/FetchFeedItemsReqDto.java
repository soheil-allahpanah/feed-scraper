package ir.sooall.feedscraper.adaptor.in.controller.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import ir.sooall.feedscraper.Application;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FetchFeedItemsReqDto {
    private @NotNull Long userId;
    private @NotNull Long feedId;
    private @NotNull @Min(10) Long fetchSize;
    private @NotBlank String jobId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime fromDate;
}
