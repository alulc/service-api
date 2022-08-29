package com.devtest;

import com.devtest.api.model.Endpoint;
import com.devtest.api.model.Service;
import com.devtest.persistence.model.EndpointRecord;
import com.devtest.persistence.model.ServiceRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static ServiceRecord serviceRecord() {
        return serviceRecord(null);
    }

    public static ServiceRecord serviceRecord(Long id) {
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(id);
        serviceRecord.setUuid(UUID.randomUUID());
        serviceRecord.setName("service");
        serviceRecord.setDescription("This service does something cool.");
        serviceRecord.setSpecificationName("spec");
        serviceRecord.setSpecificationVersion("v2.0");
        serviceRecord.setRequestCount(1L);

        List<EndpointRecord> endpointRecords = new ArrayList<>();
        EndpointRecord endpointRecord = new EndpointRecord();
        for (Endpoint.HttpVerb httpVerb: Endpoint.HttpVerb.values()) {
            endpointRecord.setUuid(UUID.randomUUID());
            endpointRecord.setHttpVerb(httpVerb.name());
            endpointRecord.setUrl("http://localhost/api/v1/test");
            endpointRecord.setOauth2Supported(true);
            endpointRecord.setOauth1aSupported(false);
            endpointRecords.add(endpointRecord);
        }
        serviceRecord.setEndpointRecords(endpointRecords);
        return serviceRecord;
    }

    public static Service service() {
        return Service.newBuilder()
                .withId(UUID.randomUUID())
                .withName("name")
                .withDescription("description")
                .withSpecificationName("spec")
                .withSpecificationVersion("version")
                .withRequestCount(0L)
                .withEndpoints(new ArrayList<>())
                .build();
    }
}
