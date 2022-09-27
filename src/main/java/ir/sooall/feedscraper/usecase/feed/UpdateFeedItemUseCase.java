package ir.sooall.feedscraper.usecase.feed;

import ir.sooall.feedscraper.domain.core.entity.Feed;
import ir.sooall.feedscraper.domain.core.entity.FeedUpdatingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UpdateFeedItemUseCase {
    @Transactional
    void updateItems(Feed feed, Optional<FeedUpdatingStatus> updatingStatusOptional);
}
