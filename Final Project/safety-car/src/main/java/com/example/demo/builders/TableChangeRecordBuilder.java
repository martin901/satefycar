package com.example.demo.builders;

import com.example.demo.models.TableChangeRecord;

import java.util.Date;

public class TableChangeRecordBuilder {
    private Date dateOfChange;

    public TableChangeRecordBuilder withDateOfChange(Date dateOfChange){
        this.dateOfChange = dateOfChange;
        return this;
    }

    public TableChangeRecord build(){
        return new TableChangeRecord(dateOfChange);
    }
}
