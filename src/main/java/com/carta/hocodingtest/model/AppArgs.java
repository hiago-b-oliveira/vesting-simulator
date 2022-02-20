package com.carta.hocodingtest.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Data
public class AppArgs {

    public static final int DEFAULT_PRECISION = 0;
    public static final int MAX_PRECISION = 6;

    private final String fileName;
    private final String targetDate;
    private final int precision;

    @Builder(access = AccessLevel.PRIVATE)
    private AppArgs(String fileName, String targetDate, int precision) {
        this.fileName = fileName;
        this.targetDate = targetDate;
        this.precision = precision;
    }

    public static AppArgs fromRawArgs(String... rawArgs) throws IllegalArgumentException {
        if (rawArgs.length < 2) {
            String errorMessage = String.format("\nExpected args: file-name target-date [precision (defaults to 0)]. Received: %s\n" +
                            "Usage Examples:\n" +
                            "\tjava -jar app.jar ./samples/example1.csv 2020-03-03\n" +
                            "\tjava -jar app.jar ./samples/example3.csv 2021-01-01 1\n\n",
                    Arrays.deepToString(rawArgs));

            throw new IllegalArgumentException(errorMessage);
        }

        String rawFileName = rawArgs[0];
        String rawTargetDate = rawArgs[1];

        validateDate(rawTargetDate);
        validateFileName(rawFileName);

        return AppArgs.builder()
                .fileName(rawFileName)
                .targetDate(rawTargetDate)
                .precision(rawArgs.length >= 3 ? parsePrecision(rawArgs[2]) : DEFAULT_PRECISION)
                .build();
    }

    private static void validateFileName(String rawFileName) throws IllegalArgumentException {
        boolean exists = Files.exists(Paths.get(rawFileName));
        if (!exists) {
            String errorMessage = String.format("File does not exists: [%s]", rawFileName);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void validateDate(String rawDate) throws IllegalArgumentException {
        try {
            LocalDate.parse(rawDate);

        } catch (DateTimeParseException e) {
            String errorMessage = String.format("Invalid date: [%s]", rawDate);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    private static int parsePrecision(String rawPrecision) throws IllegalArgumentException {
        Integer precision;

        try {
            precision = Integer.valueOf(rawPrecision);
        } catch (NumberFormatException e) {
            var errorMessage = String.format("Precision must be an integer. Received: [%s]", rawPrecision);
            throw new IllegalArgumentException(errorMessage, e);
        }

        if (precision < DEFAULT_PRECISION || precision > MAX_PRECISION) {
            var errorMessage = String.format("Invalid precision value. Expected: [%d..%d] Received: [%s]",
                    DEFAULT_PRECISION, MAX_PRECISION, rawPrecision);
            throw new IllegalArgumentException(errorMessage);
        }

        return precision;
    }
}
