package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.FetchFeedItemsResponse;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.domain.repository.UpdatingStatusRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.FetchFeedItemsUseCase;

public class FetchFeedItemsUseCaseImpl implements FetchFeedItemsUseCase {

    private final FeedItemRepository feedItemRepository;
    private final UpdatingStatusRepository updatingStatusRepository;
    private final FeedSubscriptionRepository feedSubscriptionRepository;

    public FetchFeedItemsUseCaseImpl(FeedItemRepository feedItemRepository
        , UpdatingStatusRepository updatingStatusRepository
        , FeedSubscriptionRepository feedSubscriptionRepository
    ) {
        this.feedItemRepository = feedItemRepository;
        this.updatingStatusRepository = updatingStatusRepository;
        this.feedSubscriptionRepository = feedSubscriptionRepository;
    }

    @Override
    public Try<FetchFeedItemsResponse> fetchItem(FetchFeedItemsRequest request) {

        var subscriptionExist = feedSubscriptionRepository.checkSubscription(request.getUserId(), request.getFeedId());
        if (!subscriptionExist) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "userId", request.getUserId().value().toString(), "feedId", request.getFeedId().value().toString()));
        }
        var updatingStatusOptional = updatingStatusRepository.findFeedStatus(request.getFeedId());
        if (updatingStatusOptional.isEmpty()) {
            return Try.failure(new EntityNotFoundException(FeedUpdatingStatus.class, "feedId", request.getFeedId().value().toString()));
        }
        return Try.success(new FetchFeedItemsResponse(feedItemRepository.fetchFeedItems(request.getFeedId()
            , request.getFetchSize()
            , request.getToDate())));

    }

}
