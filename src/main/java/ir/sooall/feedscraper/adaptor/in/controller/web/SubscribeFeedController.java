package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.SubscribeFeedReqDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.SubscribeFeedResDto;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedResponse;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.SubscribeFeedUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class SubscribeFeedController {
    private final SubscribeFeedUseCase subscribeFeedUseCase;

    @Autowired
    public SubscribeFeedController(SubscribeFeedUseCase subscribeFeedUseCase) {
        this.subscribeFeedUseCase = subscribeFeedUseCase;
    }

    @PostMapping("/feed/subscribe")
    @ResponseBody
    public SubscribeFeedResDto subscribe(@Valid @RequestBody SubscribeFeedReqDto request) {
        return domain2dto(subscribeFeedUseCase.subscribe(dto2Domain(request)).get());
    }

    private SubscribeFeedRequest dto2Domain(SubscribeFeedReqDto dto) {
        return new SubscribeFeedRequest(new UserId(dto.getUserId()), dto.getUrl());
    }

    private SubscribeFeedResDto domain2dto(SubscribeFeedResponse domain) {
        return new SubscribeFeedResDto(domain.getFeedId().value(), domain.getSubscriptionTime());
    }
}
