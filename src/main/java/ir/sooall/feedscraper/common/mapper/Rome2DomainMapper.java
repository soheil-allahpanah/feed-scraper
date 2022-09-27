package ir.sooall.feedscraper.common.mapper;

import com.rometools.rome.feed.synd.*;
import ir.sooall.feedscraper.domain.core.entity.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Rome2DomainMapper {
    private static <R, L> L checkIfNull(R input, Supplier<L> lazyOutput) {
        if (Objects.isNull(input)) {
            return null;
        }
        return lazyOutput.get();
    }

    public static Person mapObject(SyndPerson syncPerson) {
        return checkIfNull(syncPerson, () -> new Person(syncPerson.getName()
            , syncPerson.getUri(), syncPerson.getEmail()));
    }

    public static Content mapObject(SyndContent c) {
        return checkIfNull(c, () -> new Content(c.getType(), c.getValue(), c.getMode()));
    }

    public static Enclosure mapObject(SyndEnclosure ec) {
        return checkIfNull(ec, () -> new Enclosure(ec.getUrl(), ec.getType(), ec.getLength()));
    }

    public static Link mapObject(SyndLink link) {
        return checkIfNull(link, () -> new Link(link.getHref()
            , link.getRel(), link.getType(), link.getHreflang()
            , link.getTitle(), link.getLength()));
    }

    public static Image mapObject(SyndImage i) {
        return checkIfNull(i, () -> new Image(i.getTitle()
            , i.getUrl()
            , i.getWidth()
            , i.getHeight()
            , i.getLink()
            , i.getDescription()));
    }

    public static FeedItem mapObject(SyndEntry se) {
        return FeedItem.builder()
            .id(new FeedItemId(0L))
            .uri(se.getUri())
            .link(se.getLink())
            .comments(se.getComments())
            .contentUpdatedDate(DateUtil.convert(se.getPublishedDate()))
            .title(se.getTitle())
            .description(mapObject(se.getDescription()))
            .links(mapList(se.getLinks(), Rome2DomainMapper::mapObject))
            .contents(mapList(se.getContents(), Rome2DomainMapper::mapObject))
            .enclosures(mapList(se.getEnclosures(), Rome2DomainMapper::mapObject))
            .authors(mapList(se.getAuthors(), Rome2DomainMapper::mapObject))
            .contributors(mapList(se.getContributors(), Rome2DomainMapper::mapObject))
            .build();
    }


    public static <R, L> List<L> mapList(List<R> inputs, Function<R, L> mapper) {
        if (Objects.nonNull(inputs) && !inputs.isEmpty()) {
            return inputs.stream().map(mapper).collect(Collectors.toList());
        }
        return null;
    }


    public static <R, L> List<L> mapListWithPredicate(List<R> inputs, Function<R, L> mapper, Predicate<L> filter) {
        if (Objects.nonNull(inputs) && !inputs.isEmpty()) {
            return inputs.stream()
                .map(mapper)
                .filter(filter)
                .collect(Collectors.toList());
        }
        return null;
    }

}
