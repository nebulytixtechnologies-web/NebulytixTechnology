package com.neb.dto;

import lombok.Data;

@Data
public class UpdateBankDetailsRequestDto {
    private String bankAccountNumber;
    private String bankName;
    private String pfNumber;
    private String panNumber;
    private String uanNumber;
    private String epsNumber;
    private String esiNumber;
}
