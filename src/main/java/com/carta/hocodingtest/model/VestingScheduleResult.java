package com.carta.hocodingtest.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


@EqualsAndHashCode
@ToString
public class VestingScheduleResult {

    @Getter
    private final Set<EmployeeAwardEntry> employeeAwardEntries;
    private final Map<EmployeeAwardEntry, BigDecimal> quantitiesPerEmployeeAward;

    @Builder
    public VestingScheduleResult(Set<EmployeeAwardEntry> employeeAwardEntries, Map<EmployeeAwardEntry, BigDecimal> quantitiesPerEmployeeAward) {
        this.employeeAwardEntries = Collections.unmodifiableSet(employeeAwardEntries);
        this.quantitiesPerEmployeeAward = Collections.unmodifiableMap(quantitiesPerEmployeeAward);
    }

    public BigDecimal getQuantity(EmployeeAwardEntry employeeAwardEntry) {
        return quantitiesPerEmployeeAward.get(employeeAwardEntry);
    }
}
