package ir.sooall.feedscraper.common.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import ir.sooall.feedscraper.Application;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(),
            DateTimeFormatter.ofPattern(Application.DATE_RESULT_FORMAT).withLocale(Locale.ENGLISH));
    }
}