package ir.sooall.feedscraper.adaptor.out.web;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.control.Try;
import ir.sooall.feedscraper.common.RetryNumber;
import ir.sooall.feedscraper.common.mapper.DateUtil;
import ir.sooall.feedscraper.common.mapper.Rome2DomainMapper;
import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedId;
import ir.sooall.feedscraper.domain.repository.FetchFeedItemRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class FetchFeedItemRepositoryImpl implements FetchFeedItemRepository {

    @Override
    public Feed fetchLastUpdate(String feedUri, String lastFeedItemUri) {
        try {
            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(feedUri)));
            var dateOfLastFetchedFeedItemOpt = syndFeed.getEntries()
                .stream()
                .filter(e -> Objects.equals(e.getUri(), lastFeedItemUri))
                .map(SyndEntry::getPublishedDate)
                .map(DateUtil::convert)
                .findAny();

            return Feed.builder()
                .id(new FeedId(0L))
                .authors(Rome2DomainMapper.mapList(syndFeed.getAuthors(), Rome2DomainMapper::mapObject))
                .contributors(Rome2DomainMapper.mapList(syndFeed.getContributors(), Rome2DomainMapper::mapObject))
                .description(syndFeed.getDescription())
                .docs(syndFeed.getDocs())
                .encoding(syndFeed.getEncoding())
                .feedType(syndFeed.getFeedType())
                .generator(syndFeed.getGenerator())
                .icon(Rome2DomainMapper.mapObject(syndFeed.getIcon()))
                .image(Rome2DomainMapper.mapObject(syndFeed.getImage()))
                .link(syndFeed.getLink())
                .links(Rome2DomainMapper.mapList(syndFeed.getLinks(), Rome2DomainMapper::mapObject))
                .managingEditor(syndFeed.getManagingEditor())
                .styleSheet(syndFeed.getStyleSheet())
                .title(syndFeed.getTitle())
                .webMaster(syndFeed.getWebMaster())
                .uri(feedUri)
                .entries(Rome2DomainMapper.mapListWithPredicate(syndFeed.getEntries(), Rome2DomainMapper::mapObject
                    , e -> dateOfLastFetchedFeedItemOpt.map(dateOfLastFetchedFeedItem -> e.getContentUpdatedDate().isAfter(dateOfLastFetchedFeedItem)).orElse(true)))
                .lastUpdatedTime(LocalDateTime.now())
                .build();
        } catch (FeedException | IOException e) {
            throw new IllegalStateException("couldn't parse or fetch feed's utl");
        }
    }

}
