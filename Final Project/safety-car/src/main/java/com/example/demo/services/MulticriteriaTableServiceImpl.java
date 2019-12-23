package com.example.demo.services;

import com.example.demo.models.MulticriteriaTable;
import com.example.demo.models.utils.ExcelReader;
import com.example.demo.repositories.MulticriteriaTableRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MulticriteriaTableServiceImpl implements MulticriteriaTableService {
    private static int fileExtension = 1;

    private MulticriteriaTableRepository repository;

    @Autowired
    public MulticriteriaTableServiceImpl(MulticriteriaTableRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName + fileExtension);
        multipartFile.transferTo(convertedFile);
        ExcelReader excelReader = new ExcelReader(convertedFile);
        List<MulticriteriaTable> allEntities = getAll();
        List<MulticriteriaTable> newEntities = excelReader.saveFileContent();
        if (allEntities == null) {
            for (MulticriteriaTable table : newEntities) {
                repository.save(table);
            }
        } else {
            if (allEntities.size() == newEntities.size()) {
                updateEntities(allEntities, newEntities);
            } else if(allEntities.size() < newEntities.size()){
                updateEntities(allEntities, newEntities);
                for(int i=allEntities.size(); i<newEntities.size(); i++){
                    repository.save(newEntities.get(i));
                }
            } else {
                for (int i = 0; i < newEntities.size(); i++) {
                    updateById(allEntities.get(i).getId(), newEntities.get(i));
                }
                for(int i=newEntities.size(); i<allEntities.size(); i++){
                    repository.delete(allEntities.get(i));
                }
            }
        }
        Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir") + "/" + fileName + (fileExtension+1)));
        fileExtension++;
    }

    private void updateEntities(List<MulticriteriaTable> allEntities, List<MulticriteriaTable> newEntities) {
        for (int i = 0; i < allEntities.size(); i++) {
            updateById(allEntities.get(i).getId(), newEntities.get(i));
        }
    }

    @Override
    public List<MulticriteriaTable> getAll() {
        List<MulticriteriaTable> result = new ArrayList<>();
        CollectionUtils.addAll(result, repository.findAll());
        return result;
    }

    @Override
    public void updateById(Long id, MulticriteriaTable newTable) {
        MulticriteriaTable oldTable = getById(id);

        newTable.setId(oldTable.getId());
        repository.save(newTable);
    }

    @Override
    public MulticriteriaTable getById(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new IllegalArgumentException(String.format("MulticriteriaTable entity with id %d does not exist", id));
        }
        return repository.findById(id).get();
    }
}
