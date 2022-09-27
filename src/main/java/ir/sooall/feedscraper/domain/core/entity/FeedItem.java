package ir.sooall.feedscraper.domain.core.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedItem {
    private FeedItemId id;
    private FeedId source;
    private String uri;
    private String link;
    private String comments;
    private LocalDateTime contentUpdatedDate;
    private String title;
    private Content description;
    private List<Link> links;
    private List<Content> contents;
    private List<Enclosure> enclosures;
    private List<Person> authors;
    private List<Person> contributors;
    private LocalDateTime lastUpdatedTime;
    private LocalDateTime creationTime;


}
