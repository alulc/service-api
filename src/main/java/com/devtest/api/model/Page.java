package com.devtest.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(builder = Page.Builder.class)
public class Page<T> {

    private final String cursor;
    private final Integer limit;
    private final List<T> items;

    private Page(Builder<T> builder) {
        this.cursor = builder.cursor;
        this.limit = Validate.notNull(builder.limit, "'limit' cannot be null");
        this.items = Validate.notNull(builder.items, "'items' cannot be null");
    }

    public Optional<String> getCursor() {
        return Optional.ofNullable(cursor);
    }

    public Integer getLimit() {
        return limit;
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page = (Page<?>) o;
        return Objects.equals(limit, page.limit) && Objects.equals(cursor, page.cursor) && Objects.equals(items, page.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, cursor, items);
    }

    @Override
    public String toString() {
        return "Page{" +
                "cursor='" + cursor + '\'' +
                ", limit=" + limit +
                ", items=" + items +
                '}';
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder<>();
    }

    @JsonPOJOBuilder
    public static class Builder<T> {

        private String cursor;
        private Integer limit;
        private List<T> items;

        private Builder() {}

        public Builder<T> withCursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        public Builder<T> withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder<T> withItems(List<T> items) {
            this.items = items;
            return this;
        }

        public Page<T> build() {
            return new Page<>(this);
        }
    }
}
