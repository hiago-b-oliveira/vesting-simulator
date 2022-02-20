package com.carta.hocodingtest.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VestingEventTest {


    @Test
    public void testParseNull() {
        VestingEvent vestingEvent = VestingEvent.builder().build();
        assertNull(vestingEvent.getQuantityWithPrecision(1));
    }

    @ParameterizedTest
    @MethodSource("generateTestNumbers")
    public void testPrecision(String inputString, Integer precision, String outputString) {
        VestingEvent vestingEvent = VestingEvent.builder().quantity(inputString).build();

        assertEquals(outputString, vestingEvent.getQuantityWithPrecision(precision).toString());
    }

    public static Stream<Arguments> generateTestNumbers() {
        return Stream.of(
                Arguments.of("100.5", 2, "100.50"),
                Arguments.of("100.4567", 2, "100.45"),
                Arguments.of("0", 2, "0.00"),
                Arguments.of("123.456", 0, "123")
        );
    }
}