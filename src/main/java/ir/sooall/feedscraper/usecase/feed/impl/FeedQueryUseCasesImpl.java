package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.Tuple2;
import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.FeedListRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedListResponse;
import ir.sooall.feedscraper.domain.core.dto.FeedRequest;
import ir.sooall.feedscraper.domain.core.dto.FeedResponse;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.FeedQueryUseCases;

import java.util.Objects;
import java.util.Optional;

public class FeedQueryUseCasesImpl implements FeedQueryUseCases {
    private final FeedRepository feedRepository;
    private final FeedSubscriptionRepository feedSubscriptionRepository;

    public FeedQueryUseCasesImpl(FeedRepository feedRepository, FeedSubscriptionRepository feedSubscriptionRepository) {
        this.feedRepository = feedRepository;
        this.feedSubscriptionRepository = feedSubscriptionRepository;
    }

    @Override
    public Try<FeedListResponse> getUserFeed(FeedListRequest request) {
        var subscriptionsOpt = feedSubscriptionRepository.findSubscriptions(request.getUserId()
            , request.getFetchSize(), request.getToSubscriptionTime());
        if (subscriptionsOpt.isEmpty()) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "userid", request.getUserId().value().toString()));
        }
        var feeds = subscriptionsOpt.get().stream()
            .map(subscription -> {
                if (!subscription.getSubscribed()) {
                    return null;
                }
                var feed = feedRepository.fetchFeed(subscription.getFeedId());
                if (feed.isEmpty()) {
                    return null;
                }
                return new Tuple2<>(feed.get(), subscription.getCreationTime());
            })
            .filter(Objects::nonNull)
            .toList();
        return Try.success(new FeedListResponse(feeds));
    }

    @Override
    public Try<FeedResponse> getFeed(FeedRequest request) {
        var subscriptionOpt = feedSubscriptionRepository.findSubscription(request.getUserId(), request.getFeedId());
        if (subscriptionOpt.isEmpty() || !subscriptionOpt.get().getSubscribed()) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                    , "userid", request.getUserId().value().toString()
                    , "feedId", request.getFeedId().value().toString()
                )
            );
        }
        var feedOpt = subscriptionOpt
            .map(subscription -> feedRepository.fetchFeed(subscription.getFeedId()))
            .filter(Optional::isPresent)
            .map(Optional::get);
        if (feedOpt.isEmpty()) {
            return Try.failure(new IllegalStateException(
                String.format("User %d subscribe feed %d but feed not exist anymore!!", request.getUserId().value(), request.getFeedId().value())
            ));
        }
        return Try.success(new FeedResponse(feedOpt.get()));
    }
}
