package com.carta.hocodingtest;

import com.carta.hocodingtest.model.EmployeeAwardEntry;
import com.carta.hocodingtest.model.VestingScheduleResult;
import com.carta.hocodingtest.provider.csv.CsvIoProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(args = {"./samples/example1.csv", "2020-04-01"})
@ActiveProfiles(profiles = {"dev"})
class IntegrationTest {

    @Value("${enable-parallel-stream}")
    private Boolean parallelStreamEnabled;

    @Autowired
    HoCodingTestApplication runner;

    @MockBean
    CsvIoProvider ioProvider;

    @Captor
    ArgumentCaptor<VestingScheduleResult> resultArgumentCaptor;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(ioProvider, "parallelStreamEnabled", parallelStreamEnabled);
        doCallRealMethod().when(ioProvider).getVestingEventStream(anyString());
        doNothing().when(ioProvider).showResult(resultArgumentCaptor.capture());
    }

    @Test
    @SneakyThrows
    void testStage1() {
        runner.run("./samples/example1.csv", "2020-04-01");

        VestingScheduleResult result = resultArgumentCaptor.getValue();
        assertEquals(new BigDecimal("1000"), result.getQuantity(new EmployeeAwardEntry("E001", "Alice Smith", "ISO-001")));
        assertEquals(new BigDecimal("800"), result.getQuantity(new EmployeeAwardEntry("E001", "Alice Smith", "ISO-002")));
        assertEquals(new BigDecimal("600"), result.getQuantity(new EmployeeAwardEntry("E002", "Bobby Jones", "NSO-001")));
        assertEquals(new BigDecimal("0"), result.getQuantity(new EmployeeAwardEntry("E003", "Cat Helms", "NSO-002")));
    }

    @Test
    @SneakyThrows
    void testStage2() {
        runner.run("./samples/example2.csv", "2021-01-01");

        VestingScheduleResult result = resultArgumentCaptor.getValue();
        assertEquals(new BigDecimal("300"), result.getQuantity(new EmployeeAwardEntry("E001", "Alice Smith", "ISO-001")));
    }


    @Test
    @SneakyThrows
    void testStage3() {
        runner.run("./samples/example3.csv", "2021-01-01", "1");

        VestingScheduleResult result = resultArgumentCaptor.getValue();
        assertEquals(new BigDecimal("299.8"), result.getQuantity(new EmployeeAwardEntry("E001", "Alice Smith", "ISO-001")));
        assertEquals(new BigDecimal("234.0"), result.getQuantity(new EmployeeAwardEntry("E002", "Bobby Jones", "ISO-002")));
    }

}
