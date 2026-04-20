package com.arnav.txnledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank
    private String fromAccountNumber;
    
    @NotBlank
    private String toAccountNumber;
    
    @DecimalMin("0.01")
    private BigDecimal amount;
}
