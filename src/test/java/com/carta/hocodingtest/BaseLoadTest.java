package com.carta.hocodingtest;

import com.carta.hocodingtest.provider.csv.CsvIoProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;


abstract class BaseLoadTest {

    static final String DATE = "2022-01-01";

    @Value("${enable-parallel-stream}")
    private Boolean parallelStreamEnabled;

    @Autowired
    HoCodingTestApplication runner;

    @MockBean
    CsvIoProvider ioProvider;

    public static Stream<Arguments> loadTestInputs() {
        return Stream.of(
                Arguments.of(10_000, 100),
                Arguments.of(10_000, 500),
                Arguments.of(10_000, 1_000),
                Arguments.of(10_000_000, 100),
                Arguments.of(10_000_000, 1_000),
                Arguments.of(10_000_000, 1_000_000)
        );
    }

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(ioProvider, "parallelStreamEnabled", parallelStreamEnabled);
        doCallRealMethod().when(ioProvider).getVestingEventStream(anyString());
        doNothing().when(ioProvider).showResult(any());
    }

    void loadTest(Integer numberOfLines, Integer numberOfEmployees) {

        String file = createFile(numberOfLines, numberOfEmployees);
        runner.run(file, DATE, "6");

        deleteFile(file);
    }

    @SneakyThrows
    protected void deleteFile(String file) {
        Files.delete(Path.of(file));
    }

    @SneakyThrows
    protected String createFile(long numberOfLines, int numberOfEmployees) {
        String fileName = String.format("./samples/load-test-%s.csv", Math.random());
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));

        Random rnd = new Random();

        for (int i = 0; i < numberOfLines; i++) {
            String employeeId = "E" + rnd.nextInt(numberOfEmployees);
            String quantity = String.valueOf(rnd.nextFloat());

            writeLine(fileWriter, employeeId, quantity);
        }
        fileWriter.flush();
        fileWriter.close();

        return fileName;
    }

    @SneakyThrows
    private void writeLine(BufferedWriter fileWriter, String employeeId, String quantity) {
        fileWriter.write(String.format("%s,%s,%s,%s,%s,%s\n",
                "VEST", employeeId, employeeId, "ISO-001", DATE, quantity));
    }
}
