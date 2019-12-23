package com.example.demo.models.utils;

import com.example.demo.models.MulticriteriaTable;
import com.example.demo.services.MulticriteriaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MulticriteriaTableHelper {
    private static List<MulticriteriaTable> multicriteriaTable = new ArrayList<>();

    private MulticriteriaTableService multicriteriaTableService;

    @Autowired
    public MulticriteriaTableHelper(MulticriteriaTableService multicriteriaTableService) {
        this.multicriteriaTableService = multicriteriaTableService;
    }

    public static List<MulticriteriaTable> getMulticriteriaTable() {
        return multicriteriaTable;
    }

    public void getFromDatabase(){
        multicriteriaTable = multicriteriaTableService.getAll();
    }
}
