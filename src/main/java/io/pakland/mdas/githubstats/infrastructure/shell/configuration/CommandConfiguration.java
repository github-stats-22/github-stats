package io.pakland.mdas.githubstats.infrastructure.shell.configuration;

import io.pakland.mdas.githubstats.infrastructure.shell.components.ShellOrganizationComponent;
import io.pakland.mdas.githubstats.infrastructure.shell.components.ShellTeamComponent;
import io.pakland.mdas.githubstats.infrastructure.shell.components.ShellUserComponent;
import io.pakland.mdas.githubstats.infrastructure.shell.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class CommandConfiguration {

    @Bean
    public CommandRegistration buildUserCommand() {
        CommandRegistrationBuilder builder = new BaseCommandRegistration(
            new ShellUserComponent(),
            "user",
            "Load data from the specified user",
            "<user-name>"
        );

        return new LoggerSilencerCommandRegistration(
            new FileNameCommandRegistration(
                new ToDateCommandRegistration(new FromDateCommandRegistration(builder)),
                "output-user.csv"
            )
        ).generate().build();
    }

    @Bean
    public CommandRegistration buildTeamCommand() {
        CommandRegistrationBuilder builder = new BaseCommandRegistration(
            new ShellTeamComponent(),
            "team",
            "Load data from all users of the specified team",
            "<team-name>"
        );

        return new LoggerSilencerCommandRegistration(
            new FileNameCommandRegistration(
                new ToDateCommandRegistration(new FromDateCommandRegistration(builder)),
                "output-team.csv"
            )
        ).generate().build();
    }

    @Bean
    public CommandRegistration buildOrganizationCommand() {
        CommandRegistrationBuilder builder = new BaseCommandRegistration(
            new ShellOrganizationComponent(),
            "organization",
            "Load data from all users of the specified organization, grouped by their team",
            "<organization-name>"
        );

        return new LoggerSilencerCommandRegistration(
            new FileNameCommandRegistration(
                new ToDateCommandRegistration(new FromDateCommandRegistration(builder)),
                "output-organization.csv"
            )
        ).generate().build();
    }
}
