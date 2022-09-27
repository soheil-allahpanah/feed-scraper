package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.SubscribeFeedResponse;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedRequest;
import ir.sooall.feedscraper.domain.core.dto.UnSubscribeFeedResponse;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.SubscribeFeedUseCase;
import ir.sooall.feedscraper.usecase.feed.UnBookmarkFeedItemUseCase;
import ir.sooall.feedscraper.usecase.feed.UnSubscribeFeedUseCase;
import org.springframework.transaction.annotation.Transactional;

public class FeedSubscriptionUseCasesImpl implements SubscribeFeedUseCase, UnSubscribeFeedUseCase {

    private final FeedRepository feedRepository;
    private final FeedSubscriptionRepository feedSubscriptionRepository;
    private final UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase ;

    public FeedSubscriptionUseCasesImpl(FeedRepository feedRepository
        , FeedSubscriptionRepository feedSubscriptionRepository
        , UnBookmarkFeedItemUseCase unBookmarkFeedItemUseCase) {
        this.feedRepository = feedRepository;
        this.feedSubscriptionRepository = feedSubscriptionRepository;
        this.unBookmarkFeedItemUseCase = unBookmarkFeedItemUseCase;
    }

    @Transactional
    @Override
    public Try<SubscribeFeedResponse> subscribe(SubscribeFeedRequest request) {
        var feedIdOptional = feedRepository.findFeedIdByURI(request.getUrl());
        FeedId feedId = feedIdOptional.orElseGet(() -> feedRepository.initiateFeed(request.getUrl()));
        var feedSubscriptionOpt = feedSubscriptionRepository.findSubscription(request.getUserId(), feedId);
        if (feedSubscriptionOpt.isPresent()) {
            var subscribedFeed = feedSubscriptionOpt.get().subscribe();
            feedSubscriptionRepository.updateSubscription(subscribedFeed);
            return Try.success(new SubscribeFeedResponse(feedId, subscribedFeed.getLastUpdatedTime()));
        } else {
            var subscription = FeedSubscription.initiate(feedId, request.getUserId());
            feedSubscriptionRepository.insertSubscription(subscription);
            return Try.success(new SubscribeFeedResponse(feedId, subscription.getCreationTime()));
        }
    }

    @Transactional
    @Override
    public Try<UnSubscribeFeedResponse> unSubscribe(UnSubscribeFeedRequest request) {
        var feedSubscriptionOpt = feedSubscriptionRepository.findSubscription(request.getUserId(), request.getFeedId());
        if (feedSubscriptionOpt.isEmpty()) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "userid", request.getUserId().value().toString()
                , "feedId", request.getFeedId().value().toString()));
        }
        var feedExist = feedRepository.checkFeedExist(request.getFeedId());
        if (!feedExist) {
            return Try.failure(new EntityNotFoundException(Feed.class
                , "feedId", request.getFeedId().value().toString()));
        }
        unBookmarkFeedItemUseCase.unBookmarkItemsFromUnSubscribedFeed(request.getUserId(), request.getFeedId());
        var unSubscribedFeed = feedSubscriptionOpt.get().unsubscribe();
        feedSubscriptionRepository.updateSubscription(unSubscribedFeed);
        return Try.success(new UnSubscribeFeedResponse(request.getFeedId(), unSubscribedFeed.getLastUpdatedTime()));

    }


}
