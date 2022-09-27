package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsResponse;

public interface InitiateUpdatingFeedItemsUseCase {

    Try<UpdateFeedItemsResponse> initUpdateFeedItems(UpdateFeedItemsRequest request);

}
