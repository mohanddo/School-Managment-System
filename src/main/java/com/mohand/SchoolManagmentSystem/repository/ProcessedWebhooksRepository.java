package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.ProcessedWebhooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedWebhooksRepository extends JpaRepository<ProcessedWebhooks, Long> {
    boolean existsById(String id);
}
