package ir.sooall.feedscraper.adaptor.in.controller.web;

import ir.sooall.feedscraper.adaptor.in.controller.dto.UpdateFeedItemsReqDto;
import ir.sooall.feedscraper.adaptor.in.controller.dto.UpdateFeedItemsResDto;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.usecase.feed.InitiateUpdatingFeedItemsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class UpdateFeedItemsController {

    private final InitiateUpdatingFeedItemsUseCase initiateUpdatingFeedItemsUseCase;

    @Autowired
    public UpdateFeedItemsController(InitiateUpdatingFeedItemsUseCase initiateUpdatingFeedItemsUseCase) {
        this.initiateUpdatingFeedItemsUseCase = initiateUpdatingFeedItemsUseCase;
    }

    @PutMapping("/feed/update")
    @ResponseBody
    public UpdateFeedItemsResDto update(@Valid @RequestBody UpdateFeedItemsReqDto request) {
        return domain2dto(initiateUpdatingFeedItemsUseCase.initUpdateFeedItems(dto2Domain(request)).get());
    }

    private UpdateFeedItemsRequest dto2Domain(UpdateFeedItemsReqDto request) {
        return new UpdateFeedItemsRequest(new UserId(request.getUserId()), new FeedId(request.getUserId()));
    }

    private UpdateFeedItemsResDto domain2dto(UpdateFeedItemsResponse domain) {
        return new UpdateFeedItemsResDto(domain.getRequestStatus()
            , Objects.nonNull(domain.getFeedId()) ? domain.getFeedId().value() : null
        );
    }
}
