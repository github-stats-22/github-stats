package io.pakland.mdas.githubstats.application.external;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.pakland.mdas.githubstats.application.exceptions.HttpException;
import io.pakland.mdas.githubstats.domain.entity.Commit;
import io.pakland.mdas.githubstats.domain.entity.PullRequest;
import io.pakland.mdas.githubstats.domain.entity.Repository;
import io.pakland.mdas.githubstats.domain.repository.CommitExternalRepository;
import io.pakland.mdas.githubstats.domain.repository.CommitExternalRepository.FetchCommitsFromPullRequestRequest;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class FetchCommitsFromPullRequestTest {

    private final PullRequest pullRequest = PullRequest.builder().id(1).number(1).build();
    private final Repository repository = Repository.builder().id(1).name("github-stats")
        .ownerLogin("github-stats-22").pullRequests(Set.of(pullRequest)).build();

    @BeforeEach
    public void init() {
        pullRequest.setCommits(new HashSet<>());
        pullRequest.setRepository(repository);
    }

    @Test
    public void whenValidRepository_shouldReturnTheListOfCommits()
        throws HttpException {
        Commit commitOne = Commit.builder().sha(
            "a0b3ed9d5f1356575f2b16ab8ef5d93c5ce77575"
        ).date(new Date()).additions(250).deletions(125).build();
        Commit commitTwo = Commit.builder().sha(
            "f16b593d35d6e66dc7e1c8727d4eaa829d3973ed"
        ).date(new Date()).additions(250).deletions(125).build();

        CommitExternalRepository repository = Mockito.mock(
            CommitExternalRepository.class);

        Mockito.when(repository.fetchCommitsFromPullRequest(Mockito.any(
                FetchCommitsFromPullRequestRequest.class)))
            .thenReturn(new ArrayList<>(Arrays.asList(commitOne, commitTwo)))
            .thenReturn(new ArrayList<>());

        List<Commit> response = new FetchCommitsFromPullRequest(repository).execute(
            this.pullRequest);

        ArgumentCaptor<FetchCommitsFromPullRequestRequest> captor = ArgumentCaptor.forClass(
            FetchCommitsFromPullRequestRequest.class);
        Mockito.verify(repository, Mockito.times(2)).fetchCommitsFromPullRequest(captor.capture());
        assertEquals("github-stats-22", captor.getValue().getRepositoryOwner());
        assertEquals("github-stats", captor.getValue().getRepositoryName());
        assertEquals(2, captor.getValue().getPage());
        assertEquals(100, captor.getValue().getPerPage());
        assertEquals(2, response.size());
        assertEquals(commitOne.getSha(), response.get(0).getSha());
        assertEquals(commitTwo.getSha(), response.get(1).getSha());
    }

    @Test
    public void whenRepositoryReturnsEmptyResponse_shouldReturnEmptyList() throws HttpException {
        CommitExternalRepository repository = Mockito.mock(
            CommitExternalRepository.class);
        Mockito.when(repository.fetchCommitsFromPullRequest(Mockito.any(
            FetchCommitsFromPullRequestRequest.class))).thenReturn(new ArrayList<>());

        List<Commit> response = new FetchCommitsFromPullRequest(repository).execute(
            this.pullRequest);

        ArgumentCaptor<FetchCommitsFromPullRequestRequest> captor = ArgumentCaptor.forClass(
            FetchCommitsFromPullRequestRequest.class);
        Mockito.verify(repository).fetchCommitsFromPullRequest(captor.capture());

        assertEquals("github-stats-22", captor.getValue().getRepositoryOwner());
        assertEquals("github-stats", captor.getValue().getRepositoryName());
        assertEquals(1, captor.getValue().getPage());
        assertEquals(100, captor.getValue().getPerPage());
        assertEquals(0, response.size());
    }

    @Test
    public void whenRepositoryThrowsException_shouldThrowHttpException() throws HttpException {
        CommitExternalRepository repository = Mockito.mock(
            CommitExternalRepository.class);
        Mockito.when(repository.fetchCommitsFromPullRequest(Mockito.any(
                FetchCommitsFromPullRequestRequest.class)))
            .thenThrow(new HttpException(404, "Page not found."));

        assertThrows(HttpException.class, () -> {
            new FetchCommitsFromPullRequest(repository).execute(this.pullRequest);
        });
    }
}
