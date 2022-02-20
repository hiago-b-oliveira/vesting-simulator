package com.carta.hocodingtest.provider.csv;

import com.carta.hocodingtest.model.EmployeeAwardEntry;
import com.carta.hocodingtest.model.VestingEvent;
import com.carta.hocodingtest.model.VestingScheduleResult;
import com.carta.hocodingtest.provider.IoProvider;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class CsvIoProvider implements IoProvider {

    @Value("${enable-parallel-stream}")
    private Boolean parallelStreamEnabled;

    @SneakyThrows
    @Override
    public Stream<VestingEvent> getVestingEventStream(String fileName) {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
        CsvToBean<CsvVestingEvent> reader = new CsvToBeanBuilder<CsvVestingEvent>(fileReader)
                .withType(CsvVestingEvent.class)
                .withIgnoreEmptyLine(true)
                .withSkipLines(0)
                .build();

        Stream<CsvVestingEvent> csvVestingEventStream = reader.stream();
        if (parallelStreamEnabled) {
            csvVestingEventStream = csvVestingEventStream.parallel();
        }
        Stream<VestingEvent> vestingEventStream = csvVestingEventStream.map(CsvVestingEvent::asVestingEvent);

        return vestingEventStream;
    }

    @Override
    public void showResult(VestingScheduleResult vestingScheduleResult) {
        if (Objects.isNull(vestingScheduleResult)) return;

        Stream<EmployeeAwardEntry> sortedResultKeys = vestingScheduleResult.getEmployeeAwardEntries().stream()
                .sorted(Comparator.comparing(EmployeeAwardEntry::getEmployeeId)
                        .thenComparing(EmployeeAwardEntry::getEmployeeName)
                        .thenComparing(EmployeeAwardEntry::getAwardId));

        sortedResultKeys.forEach(employeeAwardEntry -> printResultItem(employeeAwardEntry, vestingScheduleResult.getQuantity(employeeAwardEntry)));
    }

    private void printResultItem(EmployeeAwardEntry employeeAwardEntry, BigDecimal quantity) {
        String csvItemOutput = String.format("%s,%s,%s,%s",
                employeeAwardEntry.getEmployeeId(), employeeAwardEntry.getEmployeeName(),
                employeeAwardEntry.getAwardId(), quantity);

        System.out.println(csvItemOutput);
    }

}
