package ir.sooall.feedscraper.adaptor.out.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ir.sooall.feedscraper.adapter.out.db.records.Tables;
import ir.sooall.feedscraper.adapter.out.db.records.tables.records.FeedItemsRecord;
import ir.sooall.feedscraper.common.mapper.JsonUtil;
import ir.sooall.feedscraper.domain.core.entity.*;
import ir.sooall.feedscraper.domain.repository.FeedItemRepository;
import org.jooq.impl.DefaultDSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeedItemRepositoryImpl implements FeedItemRepository {

    private final DefaultDSLContext dsl;

    public FeedItemRepositoryImpl(DefaultDSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Boolean feedItemExist(FeedId feedId, FeedItemId feedItemId) {
        var pgRecord = dsl.selectFrom(Tables.FEED_ITEMS)
            .where(Tables.FEED_ITEMS.ID.eq(feedItemId.value()))
            .and(Tables.FEED_ITEMS.FEED_ID.eq(feedId.value()))
            .fetchOptional();
        return pgRecord.isPresent();
    }

    @Override
    public Optional<FeedItem> fetchFeedItem(FeedItemId feedItemId) {
        return dsl.selectFrom(Tables.FEED_ITEMS)
            .where(Tables.FEED_ITEMS.ID.eq(feedItemId.value()))
            .fetchOptional().map(this::record2Obj);
    }


    @Override
    public List<FeedItem> fetchFeedItems(FeedId feedId, Long fetchSize, LocalDateTime toDate) {
        var query = dsl.selectFrom(Tables.FEED_ITEMS)
            .where(Tables.FEED_ITEMS.FEED_ID.eq(feedId.value()));
        if (toDate != null) {
            query = query.and(Tables.FEED_ITEMS.CONTENT_UPDATED_DATE.le(toDate));
        }
        var orderedQuery = query.orderBy(Tables.FEED_ITEMS.CONTENT_UPDATED_DATE.desc());
        if (fetchSize != null) {
            return orderedQuery.limit(fetchSize.intValue()).fetch().map(this::record2Obj);
        }
        return orderedQuery.fetch().map(this::record2Obj);

    }

    @Override
    public void updateFeedItems(List<FeedItem> entries) {
        dsl.batchInsert(entries.stream().map(this::obj2Record).collect(Collectors.toList())).execute();
    }

    private FeedItem record2Obj(FeedItemsRecord record) {
        Gson gson = new Gson();
        return FeedItem.builder()
            .id(new FeedItemId(record.get(Tables.FEED_ITEMS.ID)))
            .source(new FeedId(record.get(Tables.FEED_ITEMS.FEED_ID)))
            .uri(record.get(Tables.FEED_ITEMS.URI))
            .link(record.get(Tables.FEED_ITEMS.LINK))
            .comments(record.get(Tables.FEED_ITEMS.COMMENTS))
            .contentUpdatedDate(record.get(Tables.FEED_ITEMS.CONTENT_UPDATED_DATE))
            .title(record.get(Tables.FEED_ITEMS.TITLE))
            .description(gson.fromJson(record.get(Tables.FEED_ITEMS.DESCRIPTION), Content.class))
            .links(gson.fromJson(record.get(Tables.FEED_ITEMS.LINKS), new TypeToken<List<Link>>() {
            }.getType()))
            .contents(gson.fromJson(record.get(Tables.FEED_ITEMS.COMMENTS), new TypeToken<List<Content>>() {
            }.getType()))
            .enclosures(gson.fromJson(record.get(Tables.FEED_ITEMS.ENCLOSURES), new TypeToken<List<Enclosure>>() {
            }.getType()))
            .authors(gson.fromJson(record.get(Tables.FEED_ITEMS.AUTHORS), new TypeToken<List<Person>>() {
            }.getType()))
            .contributors(gson.fromJson(record.get(Tables.FEED_ITEMS.CONTRIBUTORS), new TypeToken<List<Person>>() {
            }.getType()))
            .lastUpdatedTime(record.get(Tables.FEED_ITEMS.LAST_UPDATED_TIME))
            .creationTime(record.get(Tables.FEED_ITEMS.CREATION_TIME))
            .build();
    }

    private FeedItemsRecord obj2Record(FeedItem feedItem) {
        FeedItemsRecord record = new FeedItemsRecord();
        record.set(Tables.FEED_ITEMS.FEED_ID, Objects.nonNull(feedItem.getSource()) ? feedItem.getSource().value() : null);
        record.set(Tables.FEED_ITEMS.URI, feedItem.getUri());
        record.set(Tables.FEED_ITEMS.LINK, feedItem.getLink());
        record.set(Tables.FEED_ITEMS.COMMENTS, feedItem.getComments());
        record.set(Tables.FEED_ITEMS.CONTENT_UPDATED_DATE, feedItem.getContentUpdatedDate());
        record.set(Tables.FEED_ITEMS.TITLE, feedItem.getTitle());
        record.set(Tables.FEED_ITEMS.DESCRIPTION, JsonUtil.toJson(feedItem.getDescription()));
        record.set(Tables.FEED_ITEMS.LINKS, JsonUtil.toJson(feedItem.getLinks()));
        record.set(Tables.FEED_ITEMS.CONTENTS, JsonUtil.toJson(feedItem.getContents()));
        record.set(Tables.FEED_ITEMS.ENCLOSURES, JsonUtil.toJson(feedItem.getEnclosures()));
        record.set(Tables.FEED_ITEMS.AUTHORS, JsonUtil.toJson(feedItem.getAuthors()));
        record.set(Tables.FEED_ITEMS.CONTRIBUTORS, JsonUtil.toJson(feedItem.getContributors()));
        record.set(Tables.FEED_ITEMS.LAST_UPDATED_TIME, feedItem.getLastUpdatedTime());
        record.set(Tables.FEED_ITEMS.CREATION_TIME, feedItem.getCreationTime());
        return record;
    }
}
