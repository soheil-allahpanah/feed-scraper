package ir.sooall.feedscraper.domain.core.entity;

import java.util.Objects;

public final class Link {
    private String href;
    private String rel;
    private String type;
    private String hrefLang;
    private String title;
    private long length;

    public Link(String href, String rel, String type, String hrefLang, String title, long length) {
        this.href = href;
        this.rel = rel;
        this.type = type;
        this.hrefLang = hrefLang;
        this.title = title;
        this.length = length;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public String getTitle() {
        return title;
    }

    public long getLength() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Link) obj;
        return Objects.equals(this.href, that.href) &&
            Objects.equals(this.rel, that.rel) &&
            Objects.equals(this.type, that.type) &&
            Objects.equals(this.hrefLang, that.hrefLang) &&
            Objects.equals(this.title, that.title) &&
            this.length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, type, hrefLang, title, length);
    }

    @Override
    public String toString() {
        return "Link[" +
            "href=" + href + ", " +
            "rel=" + rel + ", " +
            "type=" + type + ", " +
            "hrefLang=" + hrefLang + ", " +
            "title=" + title + ", " +
            "length=" + length + ']';
    }

}
