package com.example.demo.servicetests;

import com.example.demo.builders.MulticriteriaTableBuilder;
import com.example.demo.models.MulticriteriaTable;
import com.example.demo.repositories.MulticriteriaTableRepository;
import com.example.demo.services.MulticriteriaTableServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MulticriteriaTableServiceTests {
    @Mock
    private MulticriteriaTableRepository multicriteriaTableRepository;
    @InjectMocks
    private MulticriteriaTableServiceImpl multicriteriaTableService;

    MulticriteriaTable table = new MulticriteriaTableBuilder()
            .withCcMin(0)
            .withCcMax(1047)
            .withCarAgeMin(0)
            .withCarAgeMax(19)
            .withBaseAmount(403.25)
            .build();

    @Test
    public void getAll_ShouldReturnAllEntities(){
        List<MulticriteriaTable> expectedResult = Collections.singletonList(table);
        when(multicriteriaTableRepository.findAll()).thenReturn(expectedResult);
        List<MulticriteriaTable> actualResult = multicriteriaTableService.getAll();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getById_ShouldReturnEntityWithSaidId(){
        when(multicriteriaTableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        MulticriteriaTable result = multicriteriaTableService.getById(table.getId());
        assertThat(result).isEqualTo(table);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_ShouldThrowIllegalArgumentExceptionWhenThereIsNoEntityPresent(){
        when(multicriteriaTableRepository.findById(1L)).thenReturn(Optional.empty());
        multicriteriaTableService.getById(1L);
    }

    @Test
    public void updateById_ShouldUpdateOldEntityWithNewEntityIfOldEntityExists(){
        when(multicriteriaTableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        MulticriteriaTable newEntity = table;
        newEntity.setBaseAmount(413.25);
        multicriteriaTableService.updateById(table.getId(), newEntity);
        verify(multicriteriaTableRepository, times(1)).save(newEntity);
    }

    @Test
    public void save_ShouldSaveTableWhenThereIsNoTableInDatabase() throws IOException {
        when(multicriteriaTableRepository.findAll()).thenReturn(Collections.emptyList());
        MockMultipartFile mockTable = new MockMultipartFile("table", "multicriteria.xlsx",
                "application/vnd.ms-excel", new ClassPathResource("multicriteria.xlsx").getInputStream());
        multicriteriaTableService.save(mockTable);
    }
}
