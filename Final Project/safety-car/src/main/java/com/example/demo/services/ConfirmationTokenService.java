package com.example.demo.services;

import com.example.demo.models.ConfirmationToken;

public interface ConfirmationTokenService {
    ConfirmationToken save(ConfirmationToken confirmationToken);
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
