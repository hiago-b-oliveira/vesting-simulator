package com.carta.hocodingtest.provider.csv;

import com.carta.hocodingtest.model.VestingEvent;
import com.carta.hocodingtest.model.VestingEventAction;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class CsvVestingEvent {

    @CsvBindByPosition(position = 0)
    private String action;

    @CsvBindByPosition(position = 1)
    private String employeeId;

    @CsvBindByPosition(position = 2)
    private String employeeName;

    @CsvBindByPosition(position = 3)
    private String awardId;

    @CsvBindByPosition(position = 4)
    private String date;

    @CsvBindByPosition(position = 5)
    private String quantity;

    public VestingEvent asVestingEvent() {
        return VestingEvent.builder()
                .action(VestingEventAction.byName(action))
                .employeeId(employeeId)
                .employeeName(employeeName)
                .awardId(awardId)
                .date(date)
                .quantity(quantity).build();
    }

}
