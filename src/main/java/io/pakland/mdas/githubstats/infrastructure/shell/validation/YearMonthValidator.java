package io.pakland.mdas.githubstats.infrastructure.shell.validation;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class YearMonthValidator implements InputValidator<String> {

    @Override
    public boolean validate(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        YearMonth inputYearMonth;

        try {
            inputYearMonth = YearMonth.parse(input, getFormatter());
        } catch (DateTimeParseException e) {
            System.err.println("Input date doesn't have correct MM/YY format");
            return false;
        }

        if (YearMonth.now().isBefore(inputYearMonth)) {
            System.err.println("Is not allowed to request a present month or future dates");
            return false;
        }

        return true;
    }

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("MM/yy");
    }

}