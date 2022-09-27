package ir.sooall.feedscraper.domain.core.entity;

import java.util.Objects;

public final class Enclosure {
    private String url;
    private String type;
    private long length;

    public Enclosure(String url, String type, long length) {
        this.url = url;
        this.type = type;
        this.length = length;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public long getLength() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Enclosure) obj;
        return Objects.equals(this.url, that.url) &&
            Objects.equals(this.type, that.type) &&
            this.length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, length);
    }

    @Override
    public String toString() {
        return "Enclosure[" +
            "url=" + url + ", " +
            "type=" + type + ", " +
            "length=" + length + ']';
    }

}
