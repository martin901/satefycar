package com.example.demo.repositories;

import com.example.demo.models.MulticriteriaTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MulticriteriaTableRepository extends CrudRepository<MulticriteriaTable, Long> {
}
