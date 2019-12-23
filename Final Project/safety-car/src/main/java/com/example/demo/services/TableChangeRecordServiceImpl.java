package com.example.demo.services;

import com.example.demo.models.TableChangeRecord;
import com.example.demo.repositories.MulticriteriaTableRepository;
import com.example.demo.repositories.TableChangeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableChangeRecordServiceImpl implements TableChangeRecordService {
    private TableChangeRecordRepository tableChangeRecordRepository;

    @Autowired
    public TableChangeRecordServiceImpl(TableChangeRecordRepository tableChangeRecordRepository) {
        this.tableChangeRecordRepository = tableChangeRecordRepository;
    }

    @Override
    public void save(TableChangeRecord tableChangeRecord) {
        tableChangeRecordRepository.save(tableChangeRecord);
    }

    @Override
    public TableChangeRecord getLastRecord() {
        return tableChangeRecordRepository.findTopByOrderByIdDesc();
    }
}
