package com.example.demo.repositories;

import com.example.demo.models.TableChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableChangeRecordRepository extends JpaRepository<TableChangeRecord, Long> {
    TableChangeRecord findTopByOrderByIdDesc();
}
