package com.project.resource_booking_system.repsotiory;

import com.project.resource_booking_system.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE (:resourceId IS NULL OR r.id = :resourceId) and r.deletedFlag = false")
    Page<Resource> findAllByIdPaginated(Long resourceId, Pageable pageable);
}