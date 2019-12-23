package com.example.demo.servicetests;

import com.example.demo.models.ConfirmationToken;
import com.example.demo.repositories.ConfirmationTokenRepository;
import com.example.demo.services.ConfirmationTokenServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationTokenServiceTests {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @InjectMocks
    private ConfirmationTokenServiceImpl confirmationTokenService;

    ConfirmationToken token = new ConfirmationToken();

    @Test
    public void save_ShouldSaveToken(){
        when(confirmationTokenRepository.save(token)).thenReturn(token);
        ConfirmationToken result = confirmationTokenService.save(token);
        assertThat(result).isEqualTo(token);
    }

    @Test
    public void findByConfirmationToken_ShouldReturnConfirmationToken(){
        when(confirmationTokenRepository.findByConfirmationToken(token.getConfirmationToken())).thenReturn(token);
        ConfirmationToken result = confirmationTokenService.findByConfirmationToken(token.getConfirmationToken());
        assertThat(result).isEqualTo(token);
    }
}
