package com.devtest.api.mapper;

import com.devtest.api.model.Endpoint;
import com.devtest.api.model.Page;
import com.devtest.api.model.Service;
import com.devtest.persistence.model.EndpointRecord;
import com.devtest.persistence.model.ServiceRecord;
import org.apache.commons.lang3.Validate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transforms persistence models to api models.
 */
public class ModelMapper {

    public ModelMapper() {}

    public Service mapToService(ServiceRecord serviceRecord) {

        Validate.notNull(serviceRecord, "'serviceRecord' cannot be null");

        List<Endpoint> endpoints = serviceRecord.getEndpointRecords().stream()
                .map(this::mapToEndpoint)
                .collect(Collectors.toList());
        Service service = Service.newBuilder()
                .withId(serviceRecord.getUuid())
                .withName(serviceRecord.getName())
                .withDescription(serviceRecord.getDescription())
                .withSpecificationName(serviceRecord.getSpecificationName())
                .withSpecificationVersion(serviceRecord.getSpecificationVersion())
                .withRequestCount(serviceRecord.getRequestCount())
                .withEndpoints(endpoints)
                .build();
        return service;
    }

    public Page<Service> mapToPage(
            String cursor,
            Integer limit,
            List<ServiceRecord> serviceRecords) {

        Validate.notNull(limit, "'limit' cannot be null");
        Validate.notNull(serviceRecords, "'serviceRecords' cannot be null");

        List<Service> services = serviceRecords.stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
        Page<Service> page = Page.<Service>newBuilder()
                .withCursor(cursor)
                .withLimit(limit)
                .withItems(services)
                .build();
        return page;
    }

    public Page<Service> mapToFinalPage(
            Integer limit,
            List<ServiceRecord> serviceRecords) {

        return mapToPage(null, limit, serviceRecords);
    }

    private Endpoint mapToEndpoint(EndpointRecord endpointRecord) {

        Validate.notNull(endpointRecord, "'endpointRecord' cannot be null");

        URI url = URI.create(endpointRecord.getUrl());
        Endpoint.HttpVerb httpVerb = mapToHttpVerb(endpointRecord.getHttpVerb());
        Endpoint endpoint = Endpoint.newBuilder()
                .withUrl(url)
                .withHttpVerb(httpVerb)
                .withOauth2Supported(endpointRecord.getOauth2Supported())
                .withOauth1aSupported(endpointRecord.getOauth1aSupported())
                .build();
        return endpoint;
    }

    private Endpoint.HttpVerb mapToHttpVerb(String httpVerb) {

        Validate.notNull(httpVerb, "'httpVerb' cannot be null");

        switch (httpVerb) {
            case "GET":
                return Endpoint.HttpVerb.GET;
            case "PUT":
                return Endpoint.HttpVerb.PUT;
            case "POST":
                return Endpoint.HttpVerb.POST;
            case "DELETE":
                return Endpoint.HttpVerb.DELETE;
            default:
                throw new RuntimeException(String.format("unexpected http verb '%s'", httpVerb));
        }
    }
}
