package com.eex.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequest {

    @NotBlank(message = "Le nom du compte est obligatoire")
    private String name;
}