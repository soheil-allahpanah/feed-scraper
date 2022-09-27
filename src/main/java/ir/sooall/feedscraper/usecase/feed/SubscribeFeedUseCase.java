package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedResponse;

public interface SubscribeFeedUseCase {
    Try<SubscribeFeedResponse> subscribe(SubscribeFeedRequest request);
}
