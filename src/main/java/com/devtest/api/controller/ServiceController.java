package com.devtest.api.controller;

import com.devtest.api.mapper.ModelMapper;
import com.devtest.api.model.Page;
import com.devtest.api.model.Service;
import com.devtest.api.util.ServiceUtils;
import com.devtest.persistence.model.ServiceRecord;
import com.devtest.persistence.repository.ServiceRecordRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private static final Logger log = LoggerFactory.getLogger(ServiceController.class);

    // represents "0" base64 encoded.
    protected static final String STARTING_CURSOR = "MA==";

    private final ServiceRecordRepository serviceRecordRepository;
    private final ModelMapper modelMapper;
    private final ServiceUtils serviceUtils;

    @Autowired
    public ServiceController(
            ServiceRecordRepository serviceRecordRepository,
            ModelMapper modelMapper,
            ServiceUtils serviceUtils) {
        this.serviceRecordRepository = Validate.notNull(serviceRecordRepository, "'serviceRecordRepository' cannot be null");
        this.modelMapper = Validate.notNull(modelMapper, "'modelMapper' cannot be null");
        this.serviceUtils = Validate.notNull(serviceUtils, "'serviceUtils' cannot be null");
    }

    @GetMapping
    public ResponseEntity<?> readServices(
            @RequestParam(defaultValue = STARTING_CURSOR) String cursor,
            @RequestParam(defaultValue = "100") Integer limit) {

        try {
            Optional<Long> decodedCursor = serviceUtils.base64Decode(cursor).flatMap(serviceUtils::toLong);
            if (!decodedCursor.isPresent()) {
                return ResponseEntity.badRequest().body(Error.of("'cursor' must be a base64 encoded numeric string"));
            }
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest().body(Error.of("'limit' must be between 1..100"));
            }

            // we're using the service record 'id' property as a cursor to paginate.
            // also, we are using limit+1 here because we want to grab an extra
            // service record for creating the next cursor.
            List<ServiceRecord> serviceRecords = serviceRecordRepository.findAllServiceRecords(
                    decodedCursor.get(),
                    PageRequest.ofSize(limit+1).withSort(Sort.by("id").ascending()));

            // we can tell if there is an extra service record based on the result size.
            Optional<ServiceRecord> extraServiceRecord = serviceRecords.size() == limit+1
                    ? Optional.of(serviceRecords.get(serviceRecords.size()-1))
                    : Optional.empty();

            // have to remove the extra service record from the results
            List<ServiceRecord> resizedServiceRecords = new ArrayList<>(serviceRecords);
            extraServiceRecord.ifPresent(resizedServiceRecords::remove);

            Optional<String> nextEncodedCursor = extraServiceRecord.map(s -> serviceUtils.base64Encode(s.getId().toString()));

            // the absence of a cursor signals this is the final page.
            Page<Service> page = nextEncodedCursor.isPresent()
                    ? modelMapper.mapToPage(nextEncodedCursor.get(), limit, resizedServiceRecords)
                    : modelMapper.mapToFinalPage(limit, resizedServiceRecords);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            log.error("internal error occurred", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readService(@PathVariable String id) {

        try {
            Optional<UUID> uuid = serviceUtils.toUuid(id);
            if (!uuid.isPresent()) {
                return ResponseEntity.badRequest().body(Error.of("'id' in path must be a uuid"));
            }

            Optional<ServiceRecord> serviceRecord = serviceRecordRepository.findByUuid(uuid.get());
            if (!serviceRecord.isPresent()) {
                log.warn("service with id '{}' not found", uuid.get());
                return ResponseEntity.notFound().build();
            }

            Service service = modelMapper.mapToService(serviceRecord.get());
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("internal error occurred", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class Error {

        private final String message;

        private Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @JsonIgnore
        public static Error of(String message) {
            return new Error(message);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Error error = (Error) o;
            return Objects.equals(message, error.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message);
        }
    }
}
