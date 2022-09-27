package ir.sooall.feedscraper.adaptor.in.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.sooall.feedscraper.Application;
import ir.sooall.feedscraper.domain.core.entity.Content;
import ir.sooall.feedscraper.domain.core.entity.Enclosure;
import ir.sooall.feedscraper.domain.core.entity.Link;
import ir.sooall.feedscraper.domain.core.entity.Person;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class FeedItemDto {
    private Long id;
    private Long feedId;
    private String uri;
    private String link;
    private String comments;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime contentUpdatedDate;
    private String title;
    private Content description;
    private List<Link> links;
    private List<Content> contents;
    private List<Enclosure> enclosures;
    private List<Person> authors;
    private List<Person> contributors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime lastUpdatedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime bookmarkedTime;
}
