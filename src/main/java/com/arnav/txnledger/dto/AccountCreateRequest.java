package com.arnav.txnledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountCreateRequest {
    @NotBlank
    private String ownerName;
    
    @DecimalMin("0.0")
    private BigDecimal initialBalance;
}
