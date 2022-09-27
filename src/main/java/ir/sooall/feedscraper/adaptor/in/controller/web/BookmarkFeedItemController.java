package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.BookmarkFeedItemReqDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.BookmarkFeedItemResDto;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.BookmarkFeedItemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class BookmarkFeedItemController {
    private final BookmarkFeedItemUseCase bookmarkFeedItemUseCase;

    @Autowired
    public BookmarkFeedItemController(BookmarkFeedItemUseCase bookmarkFeedItemUseCase) {
        this.bookmarkFeedItemUseCase = bookmarkFeedItemUseCase;
    }

    @PutMapping("/feed-item/bookmark")
    @ResponseBody
    public BookmarkFeedItemResDto bookmark(@Valid @RequestBody BookmarkFeedItemReqDto request) {
        return domain2dto(bookmarkFeedItemUseCase.bookmark(dto2Domain(request)).get());
    }

    private BookmarkFeedItemRequest dto2Domain(BookmarkFeedItemReqDto dto) {
        return new BookmarkFeedItemRequest(new UserId(dto.getUserId()), new FeedId(dto.getFeedId()), new FeedItemId(dto.getFeedItemId()));
    }

    private BookmarkFeedItemResDto domain2dto(BookmarkFeedItemResponse domain) {
        return new BookmarkFeedItemResDto(domain.getFeedItemId().value(), domain.getLastUpdatedDate());
    }
}

