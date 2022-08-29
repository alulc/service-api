package com.devtest.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="service")
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "binary(16)")
    private UUID uuid;

    private String name;

    private String description;

    @Column(name = "specification_name")
    private String specificationName;

    @Column(name = "specification_version")
    private String specificationVersion;

    @Column(name = "request_count")
    private Long requestCount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id")
    private List<EndpointRecord> endpointRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    public void setSpecificationVersion(String specificationVersion) {
        this.specificationVersion = specificationVersion;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public List<EndpointRecord> getEndpointRecords() {
        return endpointRecords;
    }

    public void setEndpointRecords(List<EndpointRecord> endpointRecords) {
        this.endpointRecords = endpointRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRecord that = (ServiceRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(specificationName, that.specificationName) && Objects.equals(specificationVersion, that.specificationVersion) && Objects.equals(requestCount, that.requestCount) && Objects.equals(endpointRecords, that.endpointRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, description, specificationName, specificationVersion, requestCount, endpointRecords);
    }

    @Override
    public String toString() {
        return "ServiceRecord{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", specificationName='" + specificationName + '\'' +
                ", specificationVersion='" + specificationVersion + '\'' +
                ", requestCount=" + requestCount +
                ", endpointRecords=" + endpointRecords +
                '}';
    }
}
