package com.devtest.api.controller;

import com.devtest.TestDataFactory;
import com.devtest.api.mapper.ModelMapper;
import com.devtest.api.model.Page;
import com.devtest.api.model.Service;
import com.devtest.api.util.ServiceUtils;
import com.devtest.persistence.model.ServiceRecord;
import com.devtest.persistence.repository.ServiceRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    @Mock
    private ServiceRecordRepository serviceRecordRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ServiceUtils serviceUtils;

    @InjectMocks
    private ServiceController serviceController;

    @Test
    public void testReadServicesExceptionThrown() {
        String cursor = "";
        Integer limit = 1;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(500, response.getStatusCodeValue());

        Mockito.verify(serviceUtils).base64Decode(cursor);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesCursorNotNumeric() {
        String cursor = "";
        Integer limit = 1;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.empty());

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ServiceController.Error.of("'cursor' must be a base64 encoded numeric string"), response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesCursorNotEncoded() {
        String cursor = "";
        Integer limit = 1;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.empty());

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ServiceController.Error.of("'cursor' must be a base64 encoded numeric string"), response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesLimitLessThanOne() {
        String cursor = "";
        Integer limit = -1;
        Long id = 0L;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.of(id));

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ServiceController.Error.of("'limit' must be between 1..100"), response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesLimitGreaterThanOneHundred() {
        String cursor = "";
        Integer limit = 101;
        Long id = 0L;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.of(id));

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ServiceController.Error.of("'limit' must be between 1..100"), response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesNextPage() {
        String cursor = "";
        Integer limit = 1;
        Long id = 0L;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.of(id));

        ServiceRecord serviceRecord = TestDataFactory.serviceRecord(1L);
        ServiceRecord extraServiceRecord = TestDataFactory.serviceRecord(2L);
        PageRequest pageRequest = PageRequest.ofSize(limit+1).withSort(Sort.by("id").ascending());
        Mockito.when(serviceRecordRepository.findAllServiceRecords(id, pageRequest))
                .thenReturn(Arrays.asList(
                        serviceRecord,
                        extraServiceRecord));

        String nextEncodedCursor = "";
        Mockito.when(serviceUtils.base64Encode(extraServiceRecord.getId().toString())).thenReturn(nextEncodedCursor);

        Page<Service> nextPage = Page.<Service>newBuilder()
                .withCursor(nextEncodedCursor)
                .withLimit(limit)
                .withItems(Arrays.asList(TestDataFactory.service()))
                .build();
        Mockito.when(modelMapper.mapToPage(nextEncodedCursor, limit, Arrays.asList(serviceRecord))).thenReturn(nextPage);

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(nextPage, response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);
        Mockito.verify(serviceUtils).base64Encode(extraServiceRecord.getId().toString());
        Mockito.verify(serviceRecordRepository).findAllServiceRecords(id, pageRequest);
        Mockito.verify(modelMapper).mapToPage(nextEncodedCursor, limit, Arrays.asList(serviceRecord));

        Mockito.verifyNoMoreInteractions(serviceUtils, modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServicesFinalPage() {
        String cursor = "";
        Integer limit = 1;
        Long id = 0L;

        Mockito.when(serviceUtils.base64Decode(cursor)).thenReturn(Optional.of(cursor));
        Mockito.when(serviceUtils.toLong(cursor)).thenReturn(Optional.of(id));

        ServiceRecord serviceRecord = TestDataFactory.serviceRecord(1L);
        PageRequest pageRequest = PageRequest.ofSize(limit+1).withSort(Sort.by("id").ascending());
        Mockito.when(serviceRecordRepository.findAllServiceRecords(id, pageRequest))
                .thenReturn(Arrays.asList(serviceRecord));

        Page<Service> finalPage = Page.<Service>newBuilder()
                .withLimit(limit)
                .withItems(Arrays.asList(TestDataFactory.service()))
                .build();
        Mockito.when(modelMapper.mapToFinalPage(limit, Arrays.asList(serviceRecord))).thenReturn(finalPage);

        ResponseEntity<?> response = serviceController.readServices(cursor, limit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(finalPage, response.getBody());

        Mockito.verify(serviceUtils).base64Decode(cursor);
        Mockito.verify(serviceUtils).toLong(cursor);
        Mockito.verify(serviceRecordRepository).findAllServiceRecords(id, pageRequest);
        Mockito.verify(modelMapper).mapToFinalPage(limit, Arrays.asList(serviceRecord));

        Mockito.verifyNoMoreInteractions(serviceUtils, modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServiceExceptionThrown() {
        String uuid = "";

        Mockito.when(serviceUtils.toUuid(uuid)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = serviceController.readService(uuid);

        assertEquals(500, response.getStatusCodeValue());

        Mockito.verify(serviceUtils).toUuid(uuid);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServiceBadUuid() {
        String uuid = "";

        Mockito.when(serviceUtils.toUuid(uuid)).thenReturn(Optional.empty());

        ResponseEntity<?> response = serviceController.readService(uuid);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ServiceController.Error.of("'id' in path must be a uuid"), response.getBody());

        Mockito.verify(serviceUtils).toUuid(uuid);

        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(modelMapper, serviceRecordRepository);
    }

    @Test
    public void testReadServiceNotFound() {
        String uuid = UUID.randomUUID().toString();

        Mockito.when(serviceUtils.toUuid(uuid)).thenReturn(Optional.of(UUID.fromString(uuid)));

        Mockito.when(serviceRecordRepository.findByUuid(UUID.fromString(uuid))).thenReturn(Optional.empty());

        ResponseEntity<?> response = serviceController.readService(uuid);

        assertEquals(404, response.getStatusCodeValue());

        Mockito.verify(serviceUtils).toUuid(uuid);
        Mockito.verify(serviceRecordRepository).findByUuid(UUID.fromString(uuid));

        Mockito.verifyNoMoreInteractions(serviceUtils, serviceRecordRepository);
        Mockito.verifyNoInteractions(modelMapper);
    }

    @Test
    public void testReadServiceFound() {
        String uuid = UUID.randomUUID().toString();

        Mockito.when(serviceUtils.toUuid(uuid)).thenReturn(Optional.of(UUID.fromString(uuid)));

        ServiceRecord serviceRecord = TestDataFactory.serviceRecord();
        Mockito.when(serviceRecordRepository.findByUuid(UUID.fromString(uuid)))
                .thenReturn(Optional.of(serviceRecord));

        Service service = TestDataFactory.service();
        Mockito.when(modelMapper.mapToService(serviceRecord)).thenReturn(service);

        ResponseEntity<?> response = serviceController.readService(uuid);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(service, response.getBody());

        Mockito.verify(serviceUtils).toUuid(uuid);
        Mockito.verify(serviceRecordRepository).findByUuid(UUID.fromString(uuid));
        Mockito.verify(modelMapper).mapToService(serviceRecord);

        Mockito.verifyNoMoreInteractions(serviceUtils, modelMapper, serviceRecordRepository);
    }
}