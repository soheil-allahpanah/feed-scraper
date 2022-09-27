package ir.sooall.feedscraper.domain.core.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Feed {
    private FeedId id;
    private String uri;
    private String title;
    private String description;
    private String encoding;
    private String feedType;
    private String link;
    private String webMaster;
    private String managingEditor;
    private String docs;
    private String generator;
    private String styleSheet;
    private List<Link> links;
    private Image icon;
    private Image image;
    private List<FeedItem> entries;
    private List<Person> authors;
    private List<Person> contributors;
    private LocalDateTime lastUpdatedTime;
    private LocalDateTime creationTime;
}
