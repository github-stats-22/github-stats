package io.pakland.mdas.githubstats.infrastructure.github.repository;

import io.pakland.mdas.githubstats.application.dto.TeamDTO;
import io.pakland.mdas.githubstats.application.dto.UserDTO;
import io.pakland.mdas.githubstats.application.exceptions.HttpException;
import io.pakland.mdas.githubstats.domain.repository.TeamExternalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

public class TeamGitHubRepository implements TeamExternalRepository {

    private final WebClientConfiguration webClientConfiguration;
    private final Logger logger = LoggerFactory.getLogger(TeamGitHubRepository.class);

    public TeamGitHubRepository(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    @Override
    public List<TeamDTO> fetchTeamsFromOrganization(Integer organizationId) throws HttpException {
        try {
            return this.webClientConfiguration.getWebClient().get()
                    .uri(String.format("/orgs/%d/teams", organizationId))
                    .retrieve()
                    .bodyToFlux(TeamDTO.class)
                    .collectList()
                    .block();
        }  catch (WebClientResponseException ex) {
            logger.error(ex.toString());
            throw new HttpException(ex.getRawStatusCode(), ex.getMessage());
        }
    }
}
