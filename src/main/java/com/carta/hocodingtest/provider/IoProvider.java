package com.carta.hocodingtest.provider;

import com.carta.hocodingtest.model.VestingEvent;
import com.carta.hocodingtest.model.VestingScheduleResult;
import lombok.SneakyThrows;

import java.util.stream.Stream;

public interface IoProvider {
    @SneakyThrows
    Stream<VestingEvent> getVestingEventStream(String fileName);

    void showResult(VestingScheduleResult vestingScheduleResult);

}
