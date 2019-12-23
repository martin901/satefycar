package com.example.demo.services;

import com.example.demo.models.TableChangeRecord;

public interface TableChangeRecordService {
    void save(TableChangeRecord tableChangeRecord);
    TableChangeRecord getLastRecord();
}
