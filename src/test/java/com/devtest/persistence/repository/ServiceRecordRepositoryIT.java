package com.devtest.persistence.repository;

import com.devtest.TestDataFactory;
import com.devtest.persistence.model.ServiceRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/schema.sql")
@Sql(executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD,scripts="classpath:/clean.sql")
public class ServiceRecordRepositoryIT {

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Test
    public void testFindByUuidFound() throws Exception {
        ServiceRecord serviceRecord = TestDataFactory.serviceRecord();
        serviceRecordRepository.save(serviceRecord);

        Optional<ServiceRecord> retrievedServiceRecord = serviceRecordRepository.findByUuid(serviceRecord.getUuid());

        assertTrue(retrievedServiceRecord.isPresent());
        assertEquals(serviceRecord, retrievedServiceRecord.get());
    }

    @Test
    public void testFindByUuidNotFound() throws Exception {
        Optional<ServiceRecord> retrievedServiceRecord = serviceRecordRepository.findByUuid(UUID.randomUUID());

        assertFalse(retrievedServiceRecord.isPresent());
    }

    @Test
    public void testFindAllServiceRecords() throws Exception {
        List<ServiceRecord> serviceRecords = Arrays.asList(
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord(),
                TestDataFactory.serviceRecord());

        serviceRecordRepository.saveAll(serviceRecords);

        for(int i = 0; i < serviceRecords.size(); i++) {
            List<ServiceRecord> retrievedServiceRecords = serviceRecordRepository.findAllServiceRecords(
                    Long.valueOf(i+1),
                    PageRequest.ofSize(1).withSort(Sort.by("id").ascending()));

            assertEquals(1, retrievedServiceRecords.size());
            assertEquals(serviceRecords.get(i), retrievedServiceRecords.get(0));
        }
    }
}