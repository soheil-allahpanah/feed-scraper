package ir.sooall.feedscraper.adaptor.out.db;

import ir.sooall.feedscraper.adapter.out.db.records.Tables;
import ir.sooall.feedscraper.adapter.out.db.records.tables.records.BookmarkedFeedItemsRecord;
import ir.sooall.feedscraper.domain.core.entity.FeedItemBookmarkInfo;
import ir.sooall.feedscraper.domain.core.entity.FeedItemBookmarkInfoId;
import ir.sooall.feedscraper.domain.core.entity.FeedItemId;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.BookmarkRepository;
import org.jooq.impl.DefaultDSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final DefaultDSLContext dsl;

    public BookmarkRepositoryImpl(DefaultDSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Boolean checkBookmarkExist(UserId userId, FeedItemId feedItemId) {
        var pgRecord = dsl.selectFrom(Tables.BOOKMARKED_FEED_ITEMS)
            .where(Tables.BOOKMARKED_FEED_ITEMS.FEED_ITEM_ID.eq(feedItemId.value()))
            .and(Tables.BOOKMARKED_FEED_ITEMS.USER_ID.eq(userId.value()))
            .fetchOptional();
        return pgRecord.isPresent();
    }

    @Override
    public Optional<List<FeedItemBookmarkInfo>> findBookmarkedItems(UserId userId, Long fetchSize, LocalDateTime toBookmarkTime) {
        var query = dsl.selectFrom(Tables.BOOKMARKED_FEED_ITEMS)
            .where(Tables.BOOKMARKED_FEED_ITEMS.USER_ID.eq(userId.value()))
            .and(Tables.BOOKMARKED_FEED_ITEMS.BOOK_MARKED.eq(true));
        if (toBookmarkTime != null) {
            query = query.and(Tables.BOOKMARKED_FEED_ITEMS.LAST_UPDATED_TIME.le(toBookmarkTime));
        }
        var orderedQuery = query.orderBy(Tables.BOOKMARKED_FEED_ITEMS.LAST_UPDATED_TIME.desc());
        List<FeedItemBookmarkInfo> result;
        if (fetchSize != null) {
            result = orderedQuery.limit(fetchSize.intValue()).fetch().map(this::record2Obj);
        } else {
            result = query.fetch().map(this::record2Obj);
        }
        return (result.isEmpty() ? Optional.empty() : Optional.of(result));
    }

    private FeedItemBookmarkInfo record2Obj(BookmarkedFeedItemsRecord record) {
        return FeedItemBookmarkInfo
            .builder()
            .id(new FeedItemBookmarkInfoId(record.get(Tables.BOOKMARKED_FEED_ITEMS.USER_ID)))
            .userId(new UserId(record.get(Tables.BOOKMARKED_FEED_ITEMS.USER_ID)))
            .feedItemId(new FeedItemId(record.get(Tables.BOOKMARKED_FEED_ITEMS.FEED_ITEM_ID)))
            .bookmarked(record.get(Tables.BOOKMARKED_FEED_ITEMS.BOOK_MARKED))
            .creationTime(record.get(Tables.BOOKMARKED_FEED_ITEMS.CREATION_TIME))
            .lastUpdatedTime(record.get(Tables.BOOKMARKED_FEED_ITEMS.LAST_UPDATED_TIME))
            .build();
    }

    @Override
    public void insertBookmark(FeedItemBookmarkInfo bookmarkInfo) {
        dsl.insertInto(Tables.BOOKMARKED_FEED_ITEMS)
            .set(Tables.BOOKMARKED_FEED_ITEMS.USER_ID, bookmarkInfo.getUserId().value())
            .set(Tables.BOOKMARKED_FEED_ITEMS.FEED_ITEM_ID, bookmarkInfo.getFeedItemId().value())
            .set(Tables.BOOKMARKED_FEED_ITEMS.BOOK_MARKED, bookmarkInfo.getBookmarked())
            .set(Tables.BOOKMARKED_FEED_ITEMS.CREATION_TIME, bookmarkInfo.getCreationTime())
            .set(Tables.BOOKMARKED_FEED_ITEMS.LAST_UPDATED_TIME, bookmarkInfo.getLastUpdatedTime())
            .execute();
    }

    @Override
    public void updateBookmark(FeedItemBookmarkInfo bookmarkInfo) {
        dsl.update(Tables.BOOKMARKED_FEED_ITEMS)
            .set(Tables.BOOKMARKED_FEED_ITEMS.BOOK_MARKED, bookmarkInfo.getBookmarked())
            .set(Tables.BOOKMARKED_FEED_ITEMS.LAST_UPDATED_TIME, bookmarkInfo.getLastUpdatedTime())
            .where(Tables.BOOKMARKED_FEED_ITEMS.USER_ID.eq(bookmarkInfo.getUserId().value()))
            .and(Tables.BOOKMARKED_FEED_ITEMS.FEED_ITEM_ID.eq(bookmarkInfo.getFeedItemId().value()))
            .execute();
    }

}
