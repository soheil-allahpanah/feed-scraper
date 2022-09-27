package ir.sooall.feedscraper.adaptor.in.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.sooall.feedscraper.Application;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UnBookmarkFeedItemResDto {
    private Long feedItemId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime lastUpdatedDate;
}
