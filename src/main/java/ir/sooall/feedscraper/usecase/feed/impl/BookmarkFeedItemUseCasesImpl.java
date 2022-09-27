package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.BookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemRequest;
import ir.sooall.feedscraper.domain.core.dto.UnBookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.entity.*;
import ir.sooall.feedscraper.domain.repository.BookmarkRepository;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.BookmarkFeedItemUseCase;
import ir.sooall.feedscraper.usecase.feed.UnBookmarkFeedItemUseCase;
import org.springframework.transaction.annotation.Transactional;

public class BookmarkFeedItemUseCasesImpl implements BookmarkFeedItemUseCase, UnBookmarkFeedItemUseCase {

    private final FeedItemRepository feedItemRepository;
    private final FeedSubscriptionRepository feedSubscriptionRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkFeedItemUseCasesImpl(FeedItemRepository feedItemRepository
        , BookmarkRepository bookmarkRepository
        , FeedSubscriptionRepository feedSubscriptionRepository) {
        this.feedItemRepository = feedItemRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.feedSubscriptionRepository = feedSubscriptionRepository;
    }

    @Transactional
    @Override
    public Try<BookmarkFeedItemResponse> bookmark(BookmarkFeedItemRequest request) {
        var subscriptionExist = feedSubscriptionRepository.checkSubscription(request.getUserId(), request.getFeedId());
        if (!subscriptionExist) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "userId", request.getUserId().value().toString()
                , "feedId", request.getFeedItemId().value().toString())
            );
        }
        var feedItemExist = feedItemRepository.feedItemExist(request.getFeedId(), request.getFeedItemId());
        if (!feedItemExist) {
            return Try.failure(new EntityNotFoundException(FeedItem.class
                    , "id", request.getFeedItemId().value().toString()
                    , "feedId", request.getFeedId().value().toString()
                )
            );
        }
        var feedItemBookmarkInfo = FeedItemBookmarkInfo.from(request);
        var bookmarkExit = bookmarkRepository.checkBookmarkExist(request.getUserId(), request.getFeedItemId());
        if (bookmarkExit) {
            bookmarkRepository.updateBookmark(feedItemBookmarkInfo);
        } else {
            feedItemBookmarkInfo.setCreationTime(feedItemBookmarkInfo.getLastUpdatedTime());
            bookmarkRepository.insertBookmark(feedItemBookmarkInfo);
        }
        return Try.success(new BookmarkFeedItemResponse(request.getFeedItemId(), feedItemBookmarkInfo.getLastUpdatedTime()));
    }

    @Transactional
    @Override
    public Try<UnBookmarkFeedItemResponse> unBookmark(UnBookmarkFeedItemRequest request) {
        var subscriptionExist = feedSubscriptionRepository.checkSubscription(request.getUserId(), request.getFeedId());
        if (!subscriptionExist) {
            return Try.failure(new EntityNotFoundException(FeedSubscription.class
                , "userId", request.getUserId().value().toString()
                , "feedId", request.getFeedItemId().value().toString())
            );
        }
        var feedItemExist = feedItemRepository.feedItemExist(request.getFeedId(), request.getFeedItemId());
        if (!feedItemExist) {
            return Try.failure(new EntityNotFoundException(FeedItem.class
                    , "id", request.getFeedItemId().value().toString()
                    , "feedId", request.getFeedId().value().toString()
                )
            );
        }
        var feedItemBookmarkInfo = FeedItemBookmarkInfo.from(request);
        var bookmarkExit = bookmarkRepository.checkBookmarkExist(request.getUserId(), request.getFeedItemId());
        if (bookmarkExit) {
            bookmarkRepository.updateBookmark(feedItemBookmarkInfo);
            return Try.success(new UnBookmarkFeedItemResponse(request.getFeedItemId(), feedItemBookmarkInfo.getLastUpdatedTime()));
        } else {
            return Try.failure(new EntityNotFoundException(FeedItemBookmarkInfo.class
                , "userid", request.getUserId().value().toString()
                , "feedItemId", request.getFeedItemId().value().toString()));
        }
    }

    @Override
    public void unBookmarkItemsFromUnSubscribedFeed(UserId userId, FeedId feedId) {
        var unSubscribingFeedItems = feedItemRepository.fetchFeedItems(feedId, null, null);
        if (!unSubscribingFeedItems.isEmpty()) {
            var unSubscribingFeedItemIds = unSubscribingFeedItems.stream().map(FeedItem::getId).toList();
            var bookmarkedItems = bookmarkRepository.findBookmarkedItems(userId, null,null);
            if (bookmarkedItems.isPresent()) {
                var bookmarkedItemsFromUnSubscribingFeed = bookmarkedItems.get().stream()
                    .filter(e -> unSubscribingFeedItemIds.contains(e.getFeedItemId()))
                    .toList();
                bookmarkedItemsFromUnSubscribingFeed.forEach(e -> {
                    e.unBookmark();
                    bookmarkRepository.updateBookmark(e);
                });
            }
        }
    }
}
