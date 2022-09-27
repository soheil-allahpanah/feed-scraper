package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.Application;
import ir.sooall.feedscraper.adaptor.in.controller.dto.FeedItemDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.FetchFeedItemsResDto;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.FetchFeedItemsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
public class FetchFeedItemsController {

    private final FetchFeedItemsUseCase fetchFeedItemsUseCase;

    @Autowired
    public FetchFeedItemsController(FetchFeedItemsUseCase fetchFeedItemsUseCase) {
        this.fetchFeedItemsUseCase = fetchFeedItemsUseCase;
    }

    @GetMapping("/users/{userId}/feeds/{feedId}/items")
    @ResponseBody
    public FetchFeedItemsResDto fetch(@NotBlank @PathVariable(name = "feedId") Long feedId
        , @NotBlank @PathVariable(name = "userId") Long userId
        , @RequestParam(name = "fetchSize", defaultValue = "10") Long fetchSize
        , @DateTimeFormat(pattern = Application.DATE_RESULT_FORMAT) @RequestParam(name = "toDate", required = false) LocalDateTime toDate) {
        return domain2dto(fetchFeedItemsUseCase.fetchItem(dto2Domain(userId, feedId, toDate, fetchSize)).get());
    }

    private FetchFeedItemsRequest dto2Domain(Long userId, Long feedId, LocalDateTime fromDate, Long fetchSize) {
        return new FetchFeedItemsRequest(new UserId(userId), new FeedId(feedId), fromDate, fetchSize);
    }

    private FetchFeedItemsResDto domain2dto(FetchFeedItemsResponse domain) {
        var mapped = domain.getItems().stream()
            .map(e -> FeedItemDto.builder()
                .id(e.getId().value())
                .feedId(e.getSource().value())
                .uri(e.getUri())
                .link(e.getLink())
                .comments(e.getComments())
                .contentUpdatedDate(e.getContentUpdatedDate())
                .title(e.getTitle())
                .description(e.getDescription())
                .links(e.getLinks())
                .contents(e.getContents())
                .enclosures(e.getEnclosures())
                .authors(e.getAuthors())
                .contributors(e.getContributors())
                .lastUpdatedTime(e.getLastUpdatedTime())
                .creationTime(e.getCreationTime()).build())
            .collect(Collectors.toList());
        return new FetchFeedItemsResDto(mapped);
    }
}
