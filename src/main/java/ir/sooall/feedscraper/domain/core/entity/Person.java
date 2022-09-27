package ir.sooall.feedscraper.domain.core.entity;

import java.util.Objects;

public final class Person {
    private String name;
    private String uri;
    private String email;

    public Person(String name, String uri, String email) {
        this.name = name;
        this.uri = uri;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Person) obj;
        return Objects.equals(this.name, that.name) &&
            Objects.equals(this.uri, that.uri) &&
            Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri, email);
    }

    @Override
    public String toString() {
        return "Person[" +
            "name=" + name + ", " +
            "uri=" + uri + ", " +
            "email=" + email + ']';
    }

}
