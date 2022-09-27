package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.UnBookmarkFeedItemReqDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.UnBookmarkFeedItemResDto;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.UnBookmarkFeedItemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
public class UnBookmarkFeedItemController {

    private final UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase;

    @Autowired
    public UnBookmarkFeedItemController(UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase) {
        this.unBookmarkFeedItemUseCase = unBookmarkFeedItemUseCase;
    }

    @PutMapping("/feed-item/unBookmark")
    @ResponseBody
    public UnBookmarkFeedItemResDto unBookmark(@Valid @RequestBody UnBookmarkFeedItemReqDto request) {
        return domain2dto(unBookmarkFeedItemUseCase.unBookmark(dto2Domain(request)).get());
    }

    private UnBookmarkFeedItemRequest dto2Domain(UnBookmarkFeedItemReqDto dto) {
        return new UnBookmarkFeedItemRequest(new UserId(dto.getUserId()), new FeedId(dto.getFeedId()), new FeedItemId(dto.getFeedItemId()));
    }

    private UnBookmarkFeedItemResDto domain2dto(UnBookmarkFeedItemResponse domain) {
        return new UnBookmarkFeedItemResDto(domain.getFeedItemId().value(), domain.getLastUpdatedDate());
    }
}
