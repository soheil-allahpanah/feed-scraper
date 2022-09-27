package ir.sooall.feedscraper.usecase.feed.impl;

import io.vavr.Tuple2;
import io.vavr.control.Try;
import ir.sooall.feedscraper.domain.core.dto.GetBookmarkFeedItemResponse;
import ir.sooall.feedscraper.domain.core.dto.GetBookmarkFeedItemsRequest;
import ir.sooall.feedscraper.domain.core.entity.FeedItemBookmarkInfo;
import ir.sooall.feedscraper.domain.repository.BookmarkRepository;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import ir.sooall.feedscraper.usecase.exception.EntityNotFoundException;
import ir.sooall.feedscraper.usecase.feed.GetBookmarkFeedItemsUseCase;

import java.util.Objects;

public class GetBookmarkFeedItemsUseCaseImpl implements GetBookmarkFeedItemsUseCase {
    private final BookmarkRepository bookmarkRepository;
    private final FeedItemRepository feedItemRepository;

    public GetBookmarkFeedItemsUseCaseImpl(BookmarkRepository bookmarkRepository, FeedItemRepository feedItemRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.feedItemRepository = feedItemRepository;
    }

    @Override
    public Try<GetBookmarkFeedItemResponse> getBookmarkedItems(GetBookmarkFeedItemsRequest request) {
        var bookmarkedItemsOpt = bookmarkRepository.findBookmarkedItems(request.getUserId()
            , request.getFetchSize()
            , request.getToBookmarkTime());
        if (bookmarkedItemsOpt.isEmpty()) {
            return Try.failure(new EntityNotFoundException(FeedItemBookmarkInfo.class
                , "userid", request.getUserId().value().toString()));
        }
        var list = bookmarkedItemsOpt.get().stream()
            .map(bookmarkInfo -> {
                var feedItem = feedItemRepository.fetchFeedItem(bookmarkInfo.getFeedItemId());
                if (feedItem.isEmpty()) {
                    return null;
                }
                return new Tuple2<>(feedItem.get(), bookmarkInfo.getCreationTime());
            })
            .filter(Objects::nonNull)
            .toList();
        return Try.success(new GetBookmarkFeedItemResponse(list));
    }
}
