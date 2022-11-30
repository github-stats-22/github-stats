package io.pakland.mdas.githubstats.domain.service;

import io.pakland.mdas.githubstats.domain.model.Repository;
import io.pakland.mdas.githubstats.domain.model.Team;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetRepositoriesByTeamTest {

    @Test
    public void givenTeam_shouldReturnListOfRepositories() {
        Repository repository = new Repository();
        repository.setId(1L);
        Team mockTeam = Mockito.mock(Team.class);
        Mockito.when(mockTeam.getRepositories()).thenReturn(List.of(repository));

        GetRepositoriesByTeam useCase = new GetRepositoriesByTeam();
        List<Repository> repositories = useCase.execute(mockTeam);

        Mockito.verify(mockTeam, Mockito.times(1)).getRepositories();
        assertEquals(repositories.get(0).getId(), repository.getId());
    }
}
