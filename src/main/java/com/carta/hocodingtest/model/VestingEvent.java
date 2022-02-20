package com.carta.hocodingtest.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Data
@Builder
public class VestingEvent {

    private final VestingEventAction action;
    private final String employeeId;
    private final String employeeName;
    private final String awardId;
    private final String date;
    private final String quantity;

    public BigDecimal getQuantityWithPrecision(int precision) {
        return Objects.isNull(quantity) ? null : new BigDecimal(quantity).setScale(precision, RoundingMode.FLOOR);
    }
}
