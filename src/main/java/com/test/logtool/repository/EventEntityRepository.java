package com.test.logtool.repository;

import com.test.logtool.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class abstracted by spring CrudRepository
 */
@Repository
public interface EventEntityRepository extends CrudRepository<EventEntity, Long> {}

