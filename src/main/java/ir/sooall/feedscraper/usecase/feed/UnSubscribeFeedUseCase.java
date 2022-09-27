package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedResponse;

public interface UnSubscribeFeedUseCase {
    Try<UnSubscribeFeedResponse> unSubscribe(UnSubscribeFeedRequest request);
}
