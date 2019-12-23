package com.example.demo.services;

import com.example.demo.models.MulticriteriaTable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MulticriteriaTableService {
    void save(MultipartFile file) throws IOException;
    List<MulticriteriaTable> getAll();
    void updateById(Long id, MulticriteriaTable table);
    MulticriteriaTable getById(Long id);
}
