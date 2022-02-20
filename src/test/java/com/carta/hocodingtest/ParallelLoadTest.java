package com.carta.hocodingtest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@EnabledIfEnvironmentVariable(named = "load_test", matches = "true")
@SpringBootTest(args = {"./samples/example1.csv", "2020-04-01"},
        properties = "enable-parallel-stream=true")
@ActiveProfiles(profiles = {"dev"})
public class ParallelLoadTest extends BaseLoadTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("loadTestInputs")
    void loadTest(Integer numberOfLines, Integer numberOfEmployees) {
        super.loadTest(numberOfLines, numberOfEmployees);
    }
}
