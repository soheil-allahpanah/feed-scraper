package ir.sooall.feedscraper.common.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class DateUtil {
    public static LocalDateTime convert(Date date) {
        return Objects.nonNull(date) ? date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime() : null;
    }
}
