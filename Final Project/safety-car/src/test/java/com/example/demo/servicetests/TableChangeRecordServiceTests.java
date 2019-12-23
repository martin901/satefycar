package com.example.demo.servicetests;

import com.example.demo.builders.TableChangeRecordBuilder;
import com.example.demo.models.TableChangeRecord;
import com.example.demo.repositories.TableChangeRecordRepository;
import com.example.demo.services.TableChangeRecordServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TableChangeRecordServiceTests {
    @Mock
    private TableChangeRecordRepository tableChangeRecordRepository;
    @InjectMocks
    private TableChangeRecordServiceImpl tableChangeRecordService;

    private TableChangeRecord tableChangeRecord = new TableChangeRecordBuilder()
            .withDateOfChange(new Date())
            .build();

    @Test
    public void save_ShouldCallSaveOnce(){
        when(tableChangeRecordRepository.save(tableChangeRecord)).thenReturn(tableChangeRecord);
        tableChangeRecordService.save(tableChangeRecord);
        verify(tableChangeRecordRepository, times(1)).save(tableChangeRecord);
    }

    @Test
    public void getLastRecord_ShouldReturnLastRecord(){
        when(tableChangeRecordRepository.findTopByOrderByIdDesc()).thenReturn(tableChangeRecord);
        TableChangeRecord actualResult = tableChangeRecordService.getLastRecord();
        assertThat(actualResult).isEqualTo(tableChangeRecord);
    }
}
