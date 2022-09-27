package ir.sooall.feedscraper.common.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Objects;

public class JsonUtil {
    private static Gson gson = new Gson();

    public static <T> JsonElement toJson(T obj) {
        return Objects.nonNull(obj) ? gson.toJsonTree(obj) : null;
    }
}
