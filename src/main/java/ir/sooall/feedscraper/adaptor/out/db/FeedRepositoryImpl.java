package ir.sooall.feedscraper.adaptor.out.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ir.sooall.feedscraper.adapter.out.db.records.Tables;
import ir.sooall.feedscraper.adapter.out.db.records.tables.records.FeedsRecord;
import ir.sooall.feedscraper.common.mapper.JsonUtil;
import ir.sooall.feedscraper.domain.core.entity.*;
import ir.sooall.feedscraper.domain.repository.FeedRepository;
import org.jooq.impl.DefaultDSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class FeedRepositoryImpl implements FeedRepository {

    private final DefaultDSLContext dsl;

    public FeedRepositoryImpl(DefaultDSLContext dsl) {
        this.dsl = dsl;
    }


    @Override
    public Optional<Feed> fetchFeed(FeedId feedId) {
        return dsl.selectFrom(Tables.FEEDS)
            .where(Tables.FEEDS.ID.eq(feedId.value()))
            .fetchOptional().map(this::recorde2Obj);
    }

    @Override
    public Boolean checkFeedExist(FeedId feedId) {
        return dsl.selectFrom(Tables.FEEDS)
            .where(Tables.FEEDS.ID.eq(feedId.value()))
            .fetchOptional().isPresent();
    }

    @Override
    public Optional<FeedId> findFeedIdByURI(String url) {
        return dsl.selectFrom(Tables.FEEDS)
            .where(Tables.FEEDS.URI.eq(url))
            .fetchOptional().map(e -> new FeedId(e.get(Tables.FEEDS.ID)));
    }

    @Override
    public FeedId initiateFeed(String url) {
        return dsl.insertInto(Tables.FEEDS)
            .set(Tables.FEEDS.URI, url)
            .set(Tables.FEEDS.CREATION_TIME, LocalDateTime.now())
            .set(Tables.FEEDS.LAST_UPDATED_TIME, LocalDateTime.now())
            .returningResult(Tables.FEEDS.ID)
            .fetchOne().map(e -> new FeedId(e.get(Tables.FEEDS.ID)));
    }


    @Override
    public void updateFeed(Feed localFeed) {
        dsl.update(Tables.FEEDS).set(obj2Record(localFeed))
            .where(Tables.FEEDS.ID.eq(localFeed.getId().value()))
            .execute();
    }

    private Feed recorde2Obj(FeedsRecord feedsRecord) {
        Gson gson = new Gson();
        return Feed.builder()
            .id(new FeedId(feedsRecord.get(Tables.FEEDS.ID)))
            .uri(feedsRecord.get(Tables.FEEDS.URI))
            .title(feedsRecord.get(Tables.FEEDS.TITLE))
            .description(feedsRecord.get(Tables.FEEDS.DESCRIPTION))
            .encoding(feedsRecord.get(Tables.FEEDS.ENCODING))
            .feedType(feedsRecord.get(Tables.FEEDS.FEED_TYPE))
            .link(feedsRecord.get(Tables.FEEDS.LINK))
            .webMaster(feedsRecord.get(Tables.FEEDS.WEB_MASTER))
            .managingEditor(feedsRecord.get(Tables.FEEDS.MANAGING_EDITOR))
            .docs(feedsRecord.get(Tables.FEEDS.DOCS))
            .generator(feedsRecord.get(Tables.FEEDS.GENERATOR))
            .styleSheet(feedsRecord.get(Tables.FEEDS.STYLE_SHEET))
            .links(gson.fromJson(feedsRecord.get(Tables.FEEDS.LINKS), new TypeToken<List<Link>>() {
            }.getType()))
            .icon(gson.fromJson(feedsRecord.get(Tables.FEEDS.ICON), Image.class))
            .image(gson.fromJson(feedsRecord.get(Tables.FEEDS.IMAGE), Image.class))
            .authors(gson.fromJson(feedsRecord.get(Tables.FEEDS.AUTHORS), new TypeToken<List<Person>>() {
            }.getType()))
            .contributors(gson.fromJson(feedsRecord.get(Tables.FEEDS.CONTRIBUTORS), new TypeToken<List<Person>>() {
            }.getType()))
            .lastUpdatedTime(feedsRecord.get(Tables.FEEDS.LAST_UPDATED_TIME))
            .creationTime(feedsRecord.get(Tables.FEEDS.CREATION_TIME))
            .build();
    }

    private FeedsRecord obj2Record(Feed feed) {
        Gson gson = new Gson();
        FeedsRecord record = new FeedsRecord();
        record.set(Tables.FEEDS.URI, feed.getUri());
        record.set(Tables.FEEDS.TITLE, feed.getTitle());
        record.set(Tables.FEEDS.DESCRIPTION, feed.getDescription());
        record.set(Tables.FEEDS.ENCODING, feed.getEncoding());
        record.set(Tables.FEEDS.FEED_TYPE, feed.getFeedType());
        record.set(Tables.FEEDS.LINK, feed.getLink());
        record.set(Tables.FEEDS.WEB_MASTER, feed.getWebMaster());
        record.set(Tables.FEEDS.MANAGING_EDITOR, feed.getManagingEditor());
        record.set(Tables.FEEDS.DOCS, feed.getDocs());
        record.set(Tables.FEEDS.GENERATOR, feed.getGenerator());
        record.set(Tables.FEEDS.STYLE_SHEET, feed.getStyleSheet());
        record.set(Tables.FEEDS.LINKS, JsonUtil.toJson(feed.getLinks()));
        record.set(Tables.FEEDS.ICON, JsonUtil.toJson(feed.getIcon()));
        record.set(Tables.FEEDS.IMAGE, JsonUtil.toJson(feed.getImage()));
        record.set(Tables.FEEDS.AUTHORS, JsonUtil.toJson(feed.getAuthors()));
        record.set(Tables.FEEDS.CONTRIBUTORS, JsonUtil.toJson(feed.getContributors()));
        record.set(Tables.FEEDS.LAST_UPDATED_TIME, feed.getLastUpdatedTime());
        record.set(Tables.FEEDS.CREATION_TIME, feed.getCreationTime());
        return record;
    }

}
