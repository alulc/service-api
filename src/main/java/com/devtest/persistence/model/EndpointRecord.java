package com.devtest.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="endpoint")
public class EndpointRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "binary(16)")
    private UUID uuid;

    private String url;

    @Column(name = "http_verb")
    private String httpVerb;

    @Column(name = "oauth2_supported")
    private Boolean oauth2Supported;

    @Column(name = "oauth1a_supported")
    private Boolean oauth1aSupported;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public Boolean getOauth2Supported() {
        return oauth2Supported;
    }

    public void setOauth2Supported(Boolean oauth2Supported) {
        this.oauth2Supported = oauth2Supported;
    }

    public Boolean getOauth1aSupported() {
        return oauth1aSupported;
    }

    public void setOauth1aSupported(Boolean oauth1aSupported) {
        this.oauth1aSupported = oauth1aSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointRecord that = (EndpointRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(url, that.url) && Objects.equals(httpVerb, that.httpVerb) && Objects.equals(oauth2Supported, that.oauth2Supported) && Objects.equals(oauth1aSupported, that.oauth1aSupported);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, url, httpVerb, oauth2Supported, oauth1aSupported);
    }

    @Override
    public String toString() {
        return "EndpointRecord{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", url='" + url + '\'' +
                ", httpVerb='" + httpVerb + '\'' +
                ", oauth2Supported=" + oauth2Supported +
                ", oauth1aSupported=" + oauth1aSupported +
                '}';
    }
}
