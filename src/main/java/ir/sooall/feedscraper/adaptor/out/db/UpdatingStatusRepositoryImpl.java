package ir.sooall.feedscraper.adaptor.out.db;

import ir.sooall.feedscraper.adapter.out.db.records.Tables;
import ir.sooall.feedscraper.adapter.out.db.records.enums.FeedStatusType;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatusValue;
import ir.sooall.feedscraper.domain.repository.UpdatingStatusRepository;
import org.jooq.Record;
import org.jooq.impl.DefaultDSLContext;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdatingStatusRepositoryImpl implements UpdatingStatusRepository {

    private final DefaultDSLContext dsl;

    public UpdatingStatusRepositoryImpl(DefaultDSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public FeedUpdatingStatus insertAndReturnFeedStatus(FeedId feedId) {
        var now = LocalDateTime.now();
        return dsl.insertInto(Tables.FEED_STATUSES)
            .set(Tables.FEED_STATUSES.FEED_ID, feedId.value())
            .set(Tables.FEED_STATUSES.STATUS, FeedStatusType.UPDATED)
            .set(Tables.FEED_STATUSES.CREATION_TIME, now)
            .set(Tables.FEED_STATUSES.LAST_UPDATED_TIME, now)
            .returningResult(Tables.FEED_STATUSES.FEED_ID
                , Tables.FEED_STATUSES.STATUS
                , Tables.FEED_STATUSES.CREATION_TIME
                , Tables.FEED_STATUSES.LAST_FEED_ITEM_URI
                , Tables.FEED_STATUSES.LAST_UPDATED_TIME)
            .fetchOne().map(this::record2Obj);
    }


    @Override
    public Optional<FeedUpdatingStatus> findFeedStatus(FeedId feedId) {
        return dsl.selectFrom(Tables.FEED_STATUSES)
            .where(Tables.FEED_STATUSES.FEED_ID.eq(feedId.value()))
            .fetchOptional().map(this::record2Obj);
    }

    @Override
    public void updateUpdatingStatus(FeedUpdatingStatus updatingStatus) {
        dsl.update(Tables.FEED_STATUSES)
            .set(Tables.FEED_STATUSES.STATUS, FeedStatusType.valueOf(updatingStatus.getStatus().name()))
            .set(Tables.FEED_STATUSES.LAST_UPDATED_TIME, updatingStatus.getLastUpdatedTime())
            .set(Tables.FEED_STATUSES.LAST_FEED_ITEM_URI, updatingStatus.getLastFeedItemUri())
            .where(Tables.FEED_STATUSES.FEED_ID.eq(updatingStatus.getFeedId().value()))
            .execute();
    }

    private FeedUpdatingStatus record2Obj(Record record) {
        return FeedUpdatingStatus.builder()
            .feedId(new FeedId(record.get(Tables.FEED_STATUSES.FEED_ID)))
            .status(FeedUpdatingStatusValue.valueOf(record.get(Tables.FEED_STATUSES.STATUS).getLiteral()))
            .lastFeedItemUri(record.get(Tables.FEED_STATUSES.LAST_FEED_ITEM_URI))
            .creationTime(record.get(Tables.FEED_STATUSES.CREATION_TIME))
            .lastUpdatedTime(record.get(Tables.FEED_STATUSES.LAST_UPDATED_TIME))
            .build();
    }
}
