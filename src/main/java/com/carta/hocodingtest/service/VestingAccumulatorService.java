package com.carta.hocodingtest.service;

import com.carta.hocodingtest.model.EmployeeAwardEntry;
import com.carta.hocodingtest.model.VestingEvent;
import com.carta.hocodingtest.model.VestingScheduleResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Component
public class VestingAccumulatorService {

    @Value("${enable-parallel-stream}")
    private Boolean parallelStreamEnabled;

    @SneakyThrows
    public VestingScheduleResult accumulate(Stream<VestingEvent> vestingEventStream, String targetDate, int precision) {
        Map<EmployeeAwardEntry, BigDecimal> map = parallelStreamEnabled ? new ConcurrentHashMap<>() : new HashMap<>();
        BigDecimal zero = BigDecimal.ZERO.setScale(precision);

        vestingEventStream
                .forEach(vestingEvent -> {
                    EmployeeAwardEntry employeeAwardEntry = EmployeeAwardEntry.builder()
                            .employeeId(vestingEvent.getEmployeeId())
                            .employeeName(vestingEvent.getEmployeeName())
                            .awardId(vestingEvent.getAwardId()).build();

                    if (vestingEvent.getDate().compareTo(targetDate) > 0) {
                        map.putIfAbsent(employeeAwardEntry, zero);

                    } else {
                        map.compute(employeeAwardEntry, remapFunction(vestingEvent, precision));
                    }
                });

        return VestingScheduleResult.builder()
                .employeeAwardEntries(map.keySet())
                .quantitiesPerEmployeeAward(map).build();
    }

    private BiFunction<EmployeeAwardEntry, BigDecimal, BigDecimal> remapFunction(VestingEvent vestingEvent, int precision) {

        BigDecimal quantityWithPrecision = vestingEvent.getQuantityWithPrecision(precision);
        return (k, v) -> {
            if (Objects.isNull(v)) {
                v = BigDecimal.ZERO;
            }

            return vestingEvent.getAction().apply(v, quantityWithPrecision);
        };
    }
}
