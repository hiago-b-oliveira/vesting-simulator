package com.carta.hocodingtest;

import com.carta.hocodingtest.model.AppArgs;
import com.carta.hocodingtest.model.VestingEvent;
import com.carta.hocodingtest.model.VestingScheduleResult;
import com.carta.hocodingtest.provider.IoProvider;
import com.carta.hocodingtest.service.VestingAccumulatorService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class HoCodingTestApplication implements CommandLineRunner {

    private final IoProvider ioProvider;
    private final VestingAccumulatorService vestingAccumulatorService;

    public static void main(String[] args) {
        SpringApplication.run(HoCodingTestApplication.class, args);
    }

    @Override
    @SneakyThrows
    public void run(String... args) {
        try {
            StopWatch stopWatch = new StopWatch();

            log.debug("Starting app with args: {}", Arrays.deepToString(args));
            log.debug("Number of available processors: {}", Runtime.getRuntime().availableProcessors());

            var appArgs = AppArgs.fromRawArgs(args);

            stopWatch.start("reading-stream");
            Stream<VestingEvent> vestingEventStream = ioProvider.getVestingEventStream(appArgs.getFileName());
            stopWatch.stop();

            stopWatch.start("accumulating-stream");
            VestingScheduleResult vestingScheduleResult = vestingAccumulatorService.accumulate(vestingEventStream, appArgs.getTargetDate(), appArgs.getPrecision());
            stopWatch.stop();

            stopWatch.start("displaying-result");
            ioProvider.showResult(vestingScheduleResult);
            stopWatch.stop();

            log.debug("");
            log.debug("\n{}", stopWatch.prettyPrint());
            log.debug("\nElapsed Time {} ms", stopWatch.getTotalTimeMillis());

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
