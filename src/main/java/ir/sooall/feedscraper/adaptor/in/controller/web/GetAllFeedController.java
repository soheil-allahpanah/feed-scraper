package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.Application;
import ir.sooall.feedscraper.adaptor.in.controller.dto.FeedDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.FeedListResDto;
import ir.sooall.feedscraper.domain.core.dto.FeedListRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedListResponse;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.FeedQueryUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Controller
public class GetAllFeedController {

    private final FeedQueryUseCases feedQueryUseCases;

    @Autowired
    public GetAllFeedController(FeedQueryUseCases feedQueryUseCases) {
        this.feedQueryUseCases = feedQueryUseCases;
    }

    @GetMapping("/users/{userId}/feeds")
    @ResponseBody
    public FeedListResDto fetchAll(@NotBlank @PathVariable(name = "userId") Long userId
        , @RequestParam(name = "fetchSize", defaultValue = "10") Long fetchSize
        , @DateTimeFormat(pattern = Application.DATE_RESULT_FORMAT) @RequestParam(name = "toSubscriptionTime", required = false) LocalDateTime toSubscriptionTime) {
        return domain2dto(feedQueryUseCases.getUserFeed(dto2Domain(userId, fetchSize, toSubscriptionTime)).get());
    }

    private FeedListRequest dto2Domain(Long userId, Long fetchSize, LocalDateTime toSubscriptionTime) {
        return new FeedListRequest(new UserId(userId), fetchSize, toSubscriptionTime);
    }

    private FeedListResDto domain2dto(FeedListResponse domain) {
        var feedDtos = domain.getItems().stream()
            .map(tuple -> {
                    var feed = tuple._1;
                    var subscriptionTime = tuple._2;
                    return FeedDto.builder()
                        .id(feed.getId().value())
                        .uri(feed.getUri())
                        .title(feed.getTitle())
                        .description(feed.getDescription())
                        .encoding(feed.getEncoding())
                        .feedType(feed.getFeedType())
                        .link(feed.getLink())
                        .webMaster(feed.getWebMaster())
                        .managingEditor(feed.getManagingEditor())
                        .docs(feed.getDocs())
                        .generator(feed.getGenerator())
                        .styleSheet(feed.getStyleSheet())
                        .links(feed.getLinks())
                        .icon(feed.getIcon())
                        .image(feed.getImage())
                        .authors(feed.getAuthors())
                        .contributors(feed.getContributors())
                        .lastUpdatedTime(feed.getLastUpdatedTime())
                        .creationTime(feed.getCreationTime())
                        .subscriptionTime(subscriptionTime)
                        .build();
                }
            ).toList();
        return new FeedListResDto(feedDtos);
    }
}
