package com.carta.hocodingtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class EmployeeAwardEntry {

    private final String employeeId;
    private final String employeeName;
    private final String awardId;
}
