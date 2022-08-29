package com.devtest.api.mapper;

import com.devtest.TestDataFactory;
import com.devtest.api.model.Endpoint;
import com.devtest.api.model.Page;
import com.devtest.api.model.Service;
import com.devtest.persistence.model.EndpointRecord;
import com.devtest.persistence.model.ServiceRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ModelMapperTest {

    private ModelMapper modelMapper;

    @Before
    public void setup() {
        modelMapper = new ModelMapper();
    }

    @Test
    public void testMapToService() {
        ServiceRecord serviceRecord = TestDataFactory.serviceRecord();

        Service service = modelMapper.mapToService(serviceRecord);

        assertMapping(serviceRecord, service);
    }

    @Test
    public void testMapToPage() {
        Integer limit = 2;
        String cursor = "cursor";
        List<ServiceRecord> serviceRecords = Arrays.asList(
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord());

        Page<Service> page = modelMapper.mapToPage(cursor, limit, serviceRecords);

        assertEquals(limit, page.getLimit());
        assertEquals(cursor, page.getCursor().get());
        assertEquals(serviceRecords.size(), page.getItems().size());

        for (int i = 0; i < serviceRecords.size(); i++) {
            assertMapping(serviceRecords.get(i), page.getItems().get(i));
        }
    }

    @Test
    public void testMapToFinalPage() {
        Integer limit = 2;
        List<ServiceRecord> serviceRecords = Arrays.asList(
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord());

        Page<Service> page = modelMapper.mapToFinalPage(limit, serviceRecords);

        assertEquals(limit, page.getLimit());
        assertFalse(page.getCursor().isPresent());
        assertEquals(serviceRecords.size(), page.getItems().size());

        for (int i = 0; i < serviceRecords.size(); i++) {
            assertMapping(serviceRecords.get(i), page.getItems().get(i));
        }
    }

    private void assertMapping(
            ServiceRecord serviceRecord,
            Service service) {

        assertEquals(serviceRecord.getUuid(), service.getId());
        assertEquals(serviceRecord.getName(), service.getName());
        assertEquals(serviceRecord.getDescription(), service.getDescription());
        assertEquals(serviceRecord.getSpecificationName(), service.getSpecificationName());
        assertEquals(serviceRecord.getSpecificationVersion(), service.getSpecificationVersion());
        assertEquals(serviceRecord.getRequestCount(), service.getRequestCount());

        List<EndpointRecord> endpointRecords = serviceRecord.getEndpointRecords();
        List<Endpoint> endpoints = service.getEndpoints();

        assertEquals(endpointRecords.size(), endpoints.size());

        for (int i = 0; i < serviceRecord.getEndpointRecords().size(); i++) {
            assertEquals(endpointRecords.get(i).getHttpVerb(), endpoints.get(i).getHttpVerb().name());
            assertEquals(endpointRecords.get(i).getUrl(), endpoints.get(i).getUrl().toString());
            assertEquals(endpointRecords.get(i).getOauth2Supported(), endpoints.get(i).getOauth2Supported());
            assertEquals(endpointRecords.get(i).getOauth1aSupported(), endpoints.get(i).getOauth1aSupported());
        }
    }
}