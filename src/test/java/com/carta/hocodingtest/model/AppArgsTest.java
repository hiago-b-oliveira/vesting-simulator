package com.carta.hocodingtest.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AppArgsTest {

    @Test
    public void testParseSuccess() {
        AppArgs appArgs = AppArgs.fromRawArgs("./samples/example1.csv", "2020-04-01", "1");

        assertEquals(appArgs.getFileName(), "./samples/example1.csv");
        assertEquals(appArgs.getTargetDate(), "2020-04-01");
        assertEquals(appArgs.getPrecision(), 1);
    }

    @Test
    public void testParseSuccessWithDefaultPrecision() {
        AppArgs appArgs = AppArgs.fromRawArgs("./samples/example1.csv", "2020-04-01");

        assertEquals(appArgs.getFileName(), "./samples/example1.csv");
        assertEquals(appArgs.getTargetDate(), "2020-04-01");
        assertEquals(appArgs.getPrecision(), AppArgs.DEFAULT_PRECISION);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputArgs")
    void testInvalidInputs(String[] args, String errorMessage) {
        try {
            AppArgs.fromRawArgs(args);
            fail("Should throw an Exception");

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(errorMessage));
        }
    }

    public static Stream<Arguments> provideInvalidInputArgs() {
        String validFileName = "./samples/example1.csv";
        String validDate = "2020-01-01";
        return Stream.of(
                Arguments.of(new String[]{}, "Usage Examples"),
                Arguments.of(new String[]{validFileName}, "Usage Examples"),

                Arguments.of(new String[]{validFileName, validDate, "7"}, "Invalid precision value."),
                Arguments.of(new String[]{validFileName, validDate, "-1"}, "Invalid precision value."),
                Arguments.of(new String[]{validFileName, validDate, "a"}, "Precision must be an integer."),

                Arguments.of(new String[]{validFileName, "date"}, "Invalid date:"),

                Arguments.of(new String[]{"./samples/not-found.csv", validDate}, "File does not exists:")
        );
    }
}
