package ir.sooall.feedscraper.domain.repository;

import ir.sooall.feedscraper.domain.core.entity.FeedItemBookmarkInfo;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    Boolean checkBookmarkExist(UserId userId, FeedItemId feedItemId);

    Optional<List<FeedItemBookmarkInfo>> findBookmarkedItems(UserId userId, Long fetchSize, LocalDateTime toDate);

    void insertBookmark(FeedItemBookmarkInfo feedItemBookmarkInfo);

    void updateBookmark(FeedItemBookmarkInfo feedItemBookmarkInfo);

}
