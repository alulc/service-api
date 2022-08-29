package com.devtest.api.controller;

import com.devtest.TestDataFactory;
import com.devtest.api.model.Page;
import com.devtest.api.model.Service;
import com.devtest.persistence.model.ServiceRecord;
import com.devtest.persistence.repository.ServiceRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/schema.sql")
@Sql(executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD,scripts="classpath:/clean.sql")
public class ServiceControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Test
    public void testReadServiceFound() throws Exception {

        ServiceRecord serviceRecord = serviceRecordRepository.save(TestDataFactory.serviceRecord());

        ResponseEntity<Service> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/services/" + serviceRecord.getUuid().toString(),
                Service.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(serviceRecord.getUuid(), response.getBody().getId());
    }

    @Test
    public void testReadServiceNotFound() throws Exception {
        ResponseEntity<Service> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/services/" + UUID.randomUUID().toString(),
                Service.class);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testReadServices() throws Exception {

        List<ServiceRecord> serviceRecords = Arrays.asList(
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord());

        serviceRecordRepository.saveAll(serviceRecords);

        String cursor = "";
        for (int i = 0; i < serviceRecords.size(); i++) {
            ResponseEntity<Page<Service>> response = restTemplate.exchange(
                    "http://localhost:" + port + "/api/v1/services?limit=1&cursor=" + cursor,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<Service>>() {});

            Page<Service> page = response.getBody();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(Integer.valueOf(1), page.getLimit());
            assertEquals(serviceRecords.get(i).getUuid(), page.getItems().get(0).getId());

            cursor = page.getCursor().orElse(null);
        }

        // should be null after iterating
        assertNull(cursor);
    }
}
