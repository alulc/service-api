package com.devtest;

import com.devtest.api.model.Endpoint;
import com.devtest.persistence.model.EndpointRecord;
import com.devtest.persistence.model.ServiceRecord;
import com.devtest.persistence.repository.ServiceRecordRepository;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility for seeding data into the database.
 *
 * You can either supply the program argument '--seed', which will seed the db
 * with 1000 records by default. Or you can define how many records you want to
 * generate. E.g. '--seed=10000'
 *
 */
@Component
public class ApplicationDataSeedRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationDataSeedRunner.class);

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.containsOption("seed")) {

            int seedCount = Integer.parseInt(Iterables.getFirst(args.getOptionValues("seed"), "1000"));

            log.info("Seeding {} service records...", seedCount);

            for (int i = 0; i < seedCount; i++) {
                ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.setUuid(UUID.randomUUID());
                serviceRecord.setName("service-" + (i + 1));
                serviceRecord.setDescription("This service does something cool.");
                serviceRecord.setSpecificationName("spec-" + (i + 1));
                serviceRecord.setSpecificationVersion("v2.0");
                serviceRecord.setRequestCount(0L);

                List<EndpointRecord> endpointRecords = new ArrayList<>();
                for (Endpoint.HttpVerb httpVerb: Endpoint.HttpVerb.values()) {
                    EndpointRecord endpointRecord = new EndpointRecord();
                    endpointRecord.setUuid(UUID.randomUUID());
                    endpointRecord.setHttpVerb(httpVerb.name());
                    endpointRecord.setUrl("http://localhost/api/v1/test-" + (i + 1));
                    endpointRecord.setOauth2Supported(true);
                    endpointRecord.setOauth1aSupported(false);
                    endpointRecords.add(endpointRecord);
                }
                serviceRecord.setEndpointRecords(endpointRecords);

                serviceRecordRepository.save(serviceRecord);
            }
            log.info("Seed complete!");
        }
    }
}
