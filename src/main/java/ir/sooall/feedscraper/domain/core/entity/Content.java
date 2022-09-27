package ir.sooall.feedscraper.domain.core.entity;

import java.util.Objects;

public final class Content {
    private String type;
    private String value;
    private String mode;

    public Content(String type, String value, String mode) {
        this.type = type;
        this.value = value;
        this.mode = mode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Content) obj;
        return Objects.equals(this.type, that.type) &&
            Objects.equals(this.value, that.value) &&
            Objects.equals(this.mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, mode);
    }

    @Override
    public String toString() {
        return "Content[" +
            "type=" + type + ", " +
            "value=" + value + ", " +
            "mode=" + mode + ']';
    }

}
