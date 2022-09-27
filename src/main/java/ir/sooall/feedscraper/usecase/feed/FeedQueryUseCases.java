package ir.sooall.feedscraper.usecase.feed;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.FeedListRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedListResponse;
import ir.sooall.feedscraper.domain.core.dto.FeedRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedResponse;

public interface FeedQueryUseCases {
    Try<FeedListResponse> getUserFeed(FeedListRequest request);

    Try<FeedResponse> getFeed(FeedRequest request);
}
