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

public class FetchFeedItemRepositoryWithBackOffImpl implements FetchFeedItemRepository {

    private final RetryNumber retryNumber;
    private final FetchFeedItemRepository fetchFeedItemRepository;

    public FetchFeedItemRepositoryWithBackOffImpl(RetryNumber retryNumber, FetchFeedItemRepository fetchFeedItemRepository) {
        this.retryNumber = retryNumber;
        this.fetchFeedItemRepository = fetchFeedItemRepository;
    }

    @Override
    public Feed fetchLastUpdate(String feedUri, String lastFeedItemUri) {
        IntervalFunction intervalWithExponentialBackoff = IntervalFunction
            .ofExponentialRandomBackoff(IntervalFunction.DEFAULT_INITIAL_INTERVAL, 2d);
        RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(retryNumber.value())
            .retryExceptions(IllegalStateException.class, TimeoutException.class)
            .intervalFunction(intervalWithExponentialBackoff)
            .build();

        RetryRegistry registry = RetryRegistry.of(retryConfig);
        Retry retry = registry.retry("retryableFetch");
        var decoratedSupplier = Retry.decorateSupplier(retry, () -> fetchFeedItemRepository.fetchLastUpdate(feedUri, lastFeedItemUri));
        return decoratedSupplier.get();
    }

}
