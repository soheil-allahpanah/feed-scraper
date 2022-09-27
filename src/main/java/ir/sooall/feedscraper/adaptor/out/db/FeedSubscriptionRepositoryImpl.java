package ir.sooall.feedscraper.adaptor.out.db;

import ir.sooall.feedscraper.adapter.out.db.records.Tables;
import ir.sooall.feedscraper.adapter.out.db.records.tables.records.FeedSubscriptionsRecord;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.core.entity.FeedSubscription;
import ir.sooall.feedscraper.domain.core.entity.UserId;
import ir.sooall.feedscraper.domain.repository.FeedSubscriptionRepository;
import org.jooq.impl.DefaultDSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FeedSubscriptionRepositoryImpl implements FeedSubscriptionRepository {

    private final DefaultDSLContext dsl;

    public FeedSubscriptionRepositoryImpl(DefaultDSLContext dsl) {
        this.dsl = dsl;
    }


    @Override
    public Boolean checkSubscription(UserId userId, FeedId feedId) {
        return dsl.selectFrom(Tables.FEED_SUBSCRIPTIONS)
            .where(Tables.FEED_SUBSCRIPTIONS.USER_ID.eq(userId.value()))
            .and(Tables.FEED_SUBSCRIPTIONS.FEED_ID.eq(feedId.value()))
            .and(Tables.FEED_SUBSCRIPTIONS.SUBSCRIBED.eq(true))
            .fetchOptional().isPresent();

    }

    @Override
    public Optional<FeedSubscription> findSubscription(UserId userId, FeedId feedId) {
        return dsl.selectFrom(Tables.FEED_SUBSCRIPTIONS)
            .where(Tables.FEED_SUBSCRIPTIONS.USER_ID.eq(userId.value()))
            .and(Tables.FEED_SUBSCRIPTIONS.FEED_ID.eq(feedId.value()))
            .fetchOptional().map(this::record2Obj);

    }

    @Override
    public Optional<List<FeedSubscription>> findSubscriptions(UserId userId, Long fetchSize, LocalDateTime toSubscriptionTime) {
        var query = dsl.selectFrom(Tables.FEED_SUBSCRIPTIONS)
            .where(Tables.FEED_SUBSCRIPTIONS.USER_ID.eq(userId.value()));
        if (Objects.nonNull(toSubscriptionTime)) {
            query = query
                .and(Tables.FEED_SUBSCRIPTIONS.CREATION_TIME.le(toSubscriptionTime));
        }
        var result = query.orderBy(Tables.FEED_SUBSCRIPTIONS.CREATION_TIME.desc())
            .limit(fetchSize.intValue())
            .fetch().map(this::record2Obj);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    @Override
    public void insertSubscription(FeedSubscription subscription) {
        dsl.insertInto(Tables.FEED_SUBSCRIPTIONS)
            .set(Tables.FEED_SUBSCRIPTIONS.USER_ID, subscription.getUserId().value())
            .set(Tables.FEED_SUBSCRIPTIONS.FEED_ID, subscription.getFeedId().value())
            .set(Tables.FEED_SUBSCRIPTIONS.SUBSCRIBED, subscription.getSubscribed())
            .set(Tables.FEED_SUBSCRIPTIONS.CREATION_TIME, subscription.getCreationTime())
            .set(Tables.FEED_SUBSCRIPTIONS.LAST_UPDATED_TIME, subscription.getLastUpdatedTime())
            .execute();
    }

    @Override
    public void updateSubscription(FeedSubscription subscription) {
        dsl.update(Tables.FEED_SUBSCRIPTIONS)
            .set(Tables.FEED_SUBSCRIPTIONS.SUBSCRIBED, subscription.getSubscribed())
            .set(Tables.FEED_SUBSCRIPTIONS.LAST_UPDATED_TIME, subscription.getLastUpdatedTime())
            .where(Tables.FEED_SUBSCRIPTIONS.USER_ID.eq(subscription.getUserId().value()))
            .and(Tables.FEED_SUBSCRIPTIONS.FEED_ID.eq(subscription.getFeedId().value()))
            .execute();
    }

    private FeedSubscription record2Obj(FeedSubscriptionsRecord record) {
        return FeedSubscription.builder()
            .feedId(new FeedId(record.get(Tables.FEED_SUBSCRIPTIONS.FEED_ID)))
            .userId(new UserId(record.get(Tables.FEED_SUBSCRIPTIONS.USER_ID)))
            .subscribed(record.get(Tables.FEED_SUBSCRIPTIONS.SUBSCRIBED))
            .creationTime(record.get(Tables.FEED_SUBSCRIPTIONS.CREATION_TIME))
            .lastUpdatedTime(record.get(Tables.FEED_SUBSCRIPTIONS.LAST_UPDATED_TIME))
            .build();
    }
}
