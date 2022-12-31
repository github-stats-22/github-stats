package io.pakland.mdas.githubstats.infrastructure.shell.components;


import io.pakland.mdas.githubstats.domain.OptionType;
import io.pakland.mdas.githubstats.infrastructure.controller.MainController;
import io.pakland.mdas.githubstats.infrastructure.shell.model.ShellRequest;
import io.pakland.mdas.githubstats.infrastructure.shell.validation.NameValidator;
import io.pakland.mdas.githubstats.infrastructure.shell.validation.YearMonthValidator;
import org.springframework.shell.standard.ShellOption;

import java.time.YearMonth;
import java.util.Objects;

@org.springframework.shell.standard.ShellComponent
public abstract class ShellComponent {

    private final MainController mainController;

    protected ShellComponent(MainController mainController) {
        this.mainController = mainController;
    }

    private void run(
        @ShellOption(value = {"n"}) String userName,
        @ShellOption(value = {"key"}) String apiKey,
        @ShellOption(value = {"from"}) String fromDate,
        @ShellOption(value = {"to"}) String toDate,
        @ShellOption(value = {"path"}) String path,
        @ShellOption(value = {"silence"}) String silence
    ) {
        YearMonthValidator yearMonthValidator = new YearMonthValidator();
        NameValidator nameValidator = new NameValidator();

        boolean isInputValid =
            yearMonthValidator.validate(fromDate) &&
                yearMonthValidator.validate(toDate) &&
                nameValidator.validate(userName);

        if (!isInputValid) {
            return;
        }

        YearMonth dateFrom = YearMonth.parse(fromDate, yearMonthValidator.getFormatter());
        YearMonth dateTo = YearMonth.parse(toDate, yearMonthValidator.getFormatter());

        ShellRequest shellRequest = ShellRequest.builder()
            .optionType(getType())
            .name(userName)
            .apiKey(apiKey)
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .filePath(path)
            .silence(Objects.equals(silence, "true"))
            .build();

        mainController.execute(shellRequest);

    }

    abstract OptionType getType();

}
