package com.devtest.api.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonDeserialize(builder = Service.Builder.class)
public class Service {

    private final UUID id;
    private final String name;
    private final String description;
    private final String specificationName;
    private final String specificationVersion;
    private final Long requestCount;
    private final List<Endpoint> endpoints;

    private Service(Builder builder) {
        this.id = Validate.notNull(builder.id, "'id' cannot be null");
        this.name = Validate.notBlank(builder.name, "'name' cannot be blank");
        this.description = Validate.notBlank(builder.description, "'description' cannot be blank");
        this.specificationName = Validate.notBlank(builder.specificationName, "'specificationName' cannot be blank");
        this.specificationVersion = Validate.notBlank(builder.specificationVersion, "'specificationVersion' cannot be blank");
        this.requestCount = Validate.notNull(builder.requestCount, "'requestCount' cannot be null");
        this.endpoints = Validate.notNull(builder.endpoints, "'endpoints' cannot be null");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id) && Objects.equals(name, service.name) && Objects.equals(description, service.description) && Objects.equals(specificationName, service.specificationName) && Objects.equals(specificationVersion, service.specificationVersion) && Objects.equals(requestCount, service.requestCount) && Objects.equals(endpoints, service.endpoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, specificationName, specificationVersion, requestCount, endpoints);
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", specificationName='" + specificationName + '\'' +
                ", specificationVersion='" + specificationVersion + '\'' +
                ", requestCount=" + requestCount +
                ", endpoints=" + endpoints +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private UUID id;
        private String name;
        private String description;
        private String specificationName;
        private String specificationVersion;
        private Long requestCount;
        private List<Endpoint> endpoints;

        private Builder() {}

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withSpecificationName(String specificationName) {
            this.specificationName = specificationName;
            return this;
        }

        public Builder withSpecificationVersion(String specificationVersion) {
            this.specificationVersion = specificationVersion;
            return this;
        }

        public Builder withRequestCount(Long requestCount) {
            this.requestCount = requestCount;
            return this;
        }

        public Builder withEndpoints(List<Endpoint> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public Service build() {
            return new Service(this);
        }
    }
}
