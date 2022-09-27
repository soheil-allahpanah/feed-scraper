package ir.sooall.feedscraper.domain.core.entity;

import java.util.Objects;

public final class Image {
    private String title;
    private String url;
    private Integer width;
    private Integer height;
    private String link;
    private String description;

    public Image(String title, String url, Integer width, Integer height, String link, String description) {
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
        this.link = link;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Image) obj;
        return Objects.equals(this.title, that.title) &&
            Objects.equals(this.url, that.url) &&
            Objects.equals(this.width, that.width) &&
            Objects.equals(this.height, that.height) &&
            Objects.equals(this.link, that.link) &&
            Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, width, height, link, description);
    }

    @Override
    public String toString() {
        return "Image[" +
            "title=" + title + ", " +
            "url=" + url + ", " +
            "width=" + width + ", " +
            "height=" + height + ", " +
            "link=" + link + ", " +
            "description=" + description + ']';
    }

}
