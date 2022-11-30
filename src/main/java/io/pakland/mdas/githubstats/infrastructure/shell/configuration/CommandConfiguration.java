package io.pakland.mdas.githubstats.infrastructure.shell.configuration;

import io.pakland.mdas.githubstats.infrastructure.shell.components.LoginComponent;
import io.pakland.mdas.githubstats.infrastructure.shell.components.TeamComponent;
import io.pakland.mdas.githubstats.infrastructure.shell.components.UserComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class CommandConfiguration {

    @Bean
    public CommandRegistration buildUserCommand() {
        UserComponent userComponent = new UserComponent();

        return CommandRegistration.builder()
                .command("user")
                .description("Get data from a specified user.")
            .withTarget()
                .method(userComponent, "user")
                .and()
            .withOption()
                .shortNames('n')
                .longNames("name")
                .label("USER_NAME")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .withOption()
                .longNames("from")
                .label("FROM_DATE")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .withOption()
                .longNames("to")
                .label("TO_DATE")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .build();
    }

    @Bean
    public CommandRegistration buildTeamCommand() {
        TeamComponent teamComponent = new TeamComponent();

        return CommandRegistration.builder()
                .command("team")
                .description("Get data from a specified team and its sub teams.")
            .withTarget()
                .method(teamComponent, "team")
                .and()
            .withOption()
                .shortNames('n')
                .longNames("name")
                .label("TEAM_NAME")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .withOption()
                .longNames("from")
                .label("FROM_DATE")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .withOption()
                .longNames("to")
                .label("TO_DATE")
                .arity(CommandRegistration.OptionArity.EXACTLY_ONE)
                .type(String.class)
                .required()
                .and()
            .build();
    }

    @Bean
    public CommandRegistration buildLoginCommand() {
        LoginComponent loginComponent = new LoginComponent();

        return CommandRegistration.builder()
                .command("login")
                .description("Log in to the Github API before making any requests.")
            .withTarget()
                .method(loginComponent, "login")
                .and()
            .withOption()
                .longNames("token")
                .label("TOKEN")
                .type(String.class)
                .required()
                .and()
            .build();
    }

}
