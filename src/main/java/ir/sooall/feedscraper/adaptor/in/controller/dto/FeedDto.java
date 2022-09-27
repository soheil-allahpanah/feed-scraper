package ir.sooall.feedscraper.adaptor.in.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.sooall.feedscraper.Application;
import ir.sooall.feedscraper.domain.core.entity.Image;
import ir.sooall.feedscraper.domain.core.entity.Link;
import ir.sooall.feedscraper.domain.core.entity.Person;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class FeedDto {
    private Long id;
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
    private List<Person> authors;
    private List<Person> contributors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime lastUpdatedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Application.DATE_RESULT_FORMAT)
    private LocalDateTime subscriptionTime;
}
