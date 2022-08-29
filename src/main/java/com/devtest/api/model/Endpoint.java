package com.devtest.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.Validate;

import java.net.URI;
import java.util.Objects;

@JsonDeserialize(builder = Endpoint.Builder.class)
public class Endpoint {

    public enum HttpVerb {
        GET,
        POST,
        PUT,
        DELETE
    }

    private final URI url;
    private final HttpVerb httpVerb;
    private final Boolean oauth2Supported;
    private final Boolean oauth1aSupported;

    private Endpoint(Builder builder) {
        this.url = Validate.notNull(builder.url, "'url' cannot be null");
        this.httpVerb = Validate.notNull(builder.httpVerb, "'httpVerb' cannot be null");
        this.oauth2Supported = Validate.notNull(builder.oauth2Supported, "'oauth2Supported' cannot be null");
        this.oauth1aSupported = Validate.notNull(builder.oauth1aSupported, "'oauth1aSupported' cannot be null");
    }

    public URI getUrl() {
        return url;
    }

    public HttpVerb getHttpVerb() {
        return httpVerb;
    }

    public Boolean getOauth2Supported() {
        return oauth2Supported;
    }

    public Boolean getOauth1aSupported() {
        return oauth1aSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(url, endpoint.url) && httpVerb == endpoint.httpVerb && Objects.equals(oauth2Supported, endpoint.oauth2Supported) && Objects.equals(oauth1aSupported, endpoint.oauth1aSupported);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpVerb, oauth2Supported, oauth1aSupported);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "url=" + url +
                ", httpVerb=" + httpVerb +
                ", oauth2Supported=" + oauth2Supported +
                ", oauth1aSupported=" + oauth1aSupported +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private URI url;
        private HttpVerb httpVerb;
        private Boolean oauth2Supported;
        private Boolean oauth1aSupported;

        private Builder() {}

        public Builder withUrl(URI url) {
            this.url = url;
            return this;
        }

        public Builder withHttpVerb(HttpVerb httpVerb) {
            this.httpVerb = httpVerb;
            return this;
        }

        public Builder withOauth2Supported(Boolean oauth2Supported) {
            this.oauth2Supported = oauth2Supported;
            return this;
        }

        public Builder withOauth1aSupported(Boolean oauth1aSupported) {
            this.oauth1aSupported = oauth1aSupported;
            return this;
        }

        public Endpoint build() {
            return new Endpoint(this);
        }
    }
}
