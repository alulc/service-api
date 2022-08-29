package com.devtest.persistence.repository;

import com.devtest.persistence.model.ServiceRecord;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@CacheConfig(cacheNames = "service")
public interface ServiceRecordRepository extends CrudRepository<ServiceRecord, Long> {

    @Cacheable
    Optional<ServiceRecord> findByUuid(UUID uuid);

    @Cacheable
    @Query("SELECT s FROM ServiceRecord s WHERE s.id >= ?1")
    List<ServiceRecord> findAllServiceRecords(Long id, PageRequest pageRequest);
}
