package ir.sooall.feedscraper.common.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ir.sooall.feedscraper.Application;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Application.DATE_RESULT_FORMAT);
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}
