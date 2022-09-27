package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.FeedDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.FeedResDto;
import ir.sooall.feedscraper.domain.core.dto.FeedRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.FeedQueryUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;

@Controller
public class GetFeedController {

    private final FeedQueryUseCases feedQueryUseCases;

    @Autowired
    public GetFeedController(FeedQueryUseCases feedQueryUseCases) {
        this.feedQueryUseCases = feedQueryUseCases;
    }

    @GetMapping("/users/{userId}/feeds/{feedId}")
    @ResponseBody
    public FeedResDto fetch(@NotBlank @PathVariable(name = "userId") Long userId
        , @NotBlank @PathVariable(name = "feedId") Long feedId) {
        return domain2dto(feedQueryUseCases.getFeed(dto2Domain(userId, feedId)).get());
    }

    private FeedRequest dto2Domain(Long userId, Long feedId) {
        return new FeedRequest(new FeedId(feedId), new UserId(userId));
    }

    private FeedResDto domain2dto(FeedResponse domain) {
        var feed = FeedDto.builder()
            .id(domain.getFeed().getId().value())
            .uri(domain.getFeed().getUri())
            .title(domain.getFeed().getTitle())
            .description(domain.getFeed().getDescription())
            .encoding(domain.getFeed().getEncoding())
            .feedType(domain.getFeed().getFeedType())
            .link(domain.getFeed().getLink())
            .webMaster(domain.getFeed().getWebMaster())
            .managingEditor(domain.getFeed().getManagingEditor())
            .docs(domain.getFeed().getDocs())
            .generator(domain.getFeed().getGenerator())
            .styleSheet(domain.getFeed().getStyleSheet())
            .links(domain.getFeed().getLinks())
            .icon(domain.getFeed().getIcon())
            .image(domain.getFeed().getImage())
            .authors(domain.getFeed().getAuthors())
            .contributors(domain.getFeed().getContributors())
            .lastUpdatedTime(domain.getFeed().getLastUpdatedTime())
            .creationTime(domain.getFeed().getCreationTime())
            .build();
        return new FeedResDto(feed);
    }
}
