package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.UnSubscribeFeedReqDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.UnSubscribeFeedResDto;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.UnSubscribeFeedUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class UnSubscribeFeedController {
    private final UnSubscribeFeedUseCase unSubscribeFeedUseCase;

    @Autowired
    public UnSubscribeFeedController(UnSubscribeFeedUseCase unSubscribeFeedUseCase) {
        this.unSubscribeFeedUseCase = unSubscribeFeedUseCase;
    }

    @PutMapping("/feed/unSubscribe")
    @ResponseBody
    public UnSubscribeFeedResDto unSubscribe(@Valid @RequestBody UnSubscribeFeedReqDto request) {
        return domain2dto(unSubscribeFeedUseCase.unSubscribe(dto2Domain(request)).get());
    }

    private UnSubscribeFeedRequest dto2Domain(UnSubscribeFeedReqDto dto) {
        return new UnSubscribeFeedRequest(new UserId(dto.getUserId()), new FeedId(dto.getFeedId()));
    }

    private UnSubscribeFeedResDto domain2dto(UnSubscribeFeedResponse domain) {
        return new UnSubscribeFeedResDto(domain.getId().value(), domain.getUnSubscriptionTime());
    }
}
