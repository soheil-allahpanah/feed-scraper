package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.RequestStatus;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.dto.UpdateFeedItemsResponse;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatusValue;
import ir.sooall.feedscraper.domain.repository.*;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.InitiateUpdatingFeedItemsUseCase;
import ir.sooall.feedscraper.usecase.feed.UpdateFeedItemUseCase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class InitiateUpdatingFeedItemsUseCaseImpl implements InitiateUpdatingFeedItemsUseCase {

    private final FeedRepository feedRepository;
    private final UpdatingStatusRepository updatingStatusRepository;
    private final FeedSubscriptionRepository feedSubscriptionRepository;
    private final UpdateFeedItemUseCase updateFeedItemUseCase;

    public InitiateUpdatingFeedItemsUseCaseImpl(FeedRepository feedRepository
        , UpdatingStatusRepository updatingStatusRepository
        , FeedSubscriptionRepository feedSubscriptionRepository
        , UpdateFeedItemUseCase updateFeedItemUseCase
    ) {
        this.feedRepository = feedRepository;
        this.updatingStatusRepository = updatingStatusRepository;
        this.feedSubscriptionRepository = feedSubscriptionRepository;
        this.updateFeedItemUseCase = updateFeedItemUseCase;
    }

    @Override
    public Try<UpdateFeedItemsResponse> initUpdateFeedItems(UpdateFeedItemsRequest request) {
        var subscriptionExist = feedSubscriptionRepository.checkSubscription(request.getUserId(), request.getFeedId());
        if (!subscriptionExist) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "feedId", request.getFeedId().value().toString(), "userId",  request.getUserId().value().toString()));
        }
        var feedOptional = feedRepository.fetchFeed(request.getFeedId());
        if (feedOptional.isEmpty()) {
            return Try.failure(new EntityNotFoundException(Feed.class, "id", request.getFeedId().value().toString()));
        }
        var feed = feedOptional.get();
        var updatingStatusOptional = updatingStatusRepository.findFeedStatus(feed.getId());
        if (updatingStatusOptional.isPresent()) {
            var updatingStatus = updatingStatusOptional.get();
            if (updatingStatus.getStatus().equals(FeedUpdatingStatusValue.UPDATING)) {
                return Try.failure(new IllegalStateException("Feed already being updating"));
            }
        }
        updateFeedItemUseCase.updateItems(feed, updatingStatusOptional);
        return Try.success(new UpdateFeedItemsResponse(RequestStatus.SUBMITTED, request.getFeedId()));
    }


}
