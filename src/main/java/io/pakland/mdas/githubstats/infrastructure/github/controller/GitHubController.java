package io.pakland.mdas.githubstats.infrastructure.github.controller;

import io.pakland.mdas.githubstats.application.exceptions.HttpException;
import io.pakland.mdas.githubstats.application.external.*;
import io.pakland.mdas.githubstats.application.internal.AggregatePullRequests;
import io.pakland.mdas.githubstats.domain.entity.*;
import io.pakland.mdas.githubstats.domain.repository.*;
import io.pakland.mdas.githubstats.infrastructure.github.model.GitHubUserOptionRequest;
import io.pakland.mdas.githubstats.infrastructure.github.repository.*;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@NoArgsConstructor
public class GitHubController {

    private WebClientConfiguration webClientConfiguration;
    private GitHubUserOptionRequest userOptionRequest;
    private OrganizationExternalRepository organizationRepository;
    private TeamExternalRepository teamRepository;
    private RepositoryExternalRepository repositoryRepository;
    private UserExternalRepository userRepository;
    private PullRequestExternalRepository pullRequestRepository;
    private ReviewExternalRepository reviewRepository;
    private CommentExternalRepository commentRepository;

    public GitHubController(GitHubUserOptionRequest userOptionRequest) {
        this.webClientConfiguration = new WebClientConfiguration(
            "https://api.github.com", userOptionRequest.getApiKey());
        this.userOptionRequest = userOptionRequest;
        this.organizationRepository = new OrganizationGitHubRepository(this.webClientConfiguration);
        this.teamRepository = new TeamGitHubRepository(this.webClientConfiguration);
        this.repositoryRepository = new RepositoryGitHubRepository(this.webClientConfiguration);
        this.userRepository = new UserGitHubRepository(this.webClientConfiguration);
        this.pullRequestRepository = new PullRequestGitHubRepository(this.webClientConfiguration);
        this.reviewRepository = new ReviewGitHubRepository(this.webClientConfiguration);
        this.commentRepository = new CommentGitHubRepository(this.webClientConfiguration);
    }

    public void execute() {
        try {
            // Fetch the API key's available organizations.
            List<Organization> organizationList =
                new FetchAvailableOrganizations(organizationRepository).execute();
            organizationList.forEach(this::fetchTeamsFromOrganization);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchTeamsFromOrganization(Organization organization) {
        try {
            List<Team> teamList = new FetchTeamsFromOrganization(teamRepository)
                .execute(organization);
            teamList.parallelStream().forEach(team -> {
                this.fetchRepositoriesFromTeam(team);
                this.fetchUsersFromTeam(team);
            });
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchRepositoriesFromTeam(Team team) {
        try {
            // Fetch the repositories for each team.
            List<Repository> repositoryList = new FetchRepositoriesFromTeam(
                repositoryRepository).execute(team);
            // Add the team to the repository
            repositoryList.parallelStream().forEach(this::fetchPullRequestsFromRepository);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchUsersFromTeam(Team team) {
        try {
            // Fetch the members of each team.
            new FetchUsersFromTeam(userRepository).execute(team);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchPullRequestsFromRepository(Repository repository) {
        try {
            // Fetch pull requests from each team.
            List<PullRequest> pullRequestList =
                new FetchPullRequestsInPeriodFromRepository(pullRequestRepository)
                    .execute(repository, userOptionRequest.getFrom(), userOptionRequest.getTo());

            ExecutorService executor = Executors.newFixedThreadPool(3);
            pullRequestList.parallelStream().forEach(pullRequest -> {

                Future<?> future2 = executor.submit(
                    () -> this.fetchReviewsFromPullRequest(pullRequest));
                Future<?> future3 = executor.submit(
                    () -> this.fetchCommentsFromPullRequest(pullRequest));

                try {
                    future2.get();
                    future3.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            Map<Team, Map<User, PullRequestAggregation>> prAggregation = new AggregatePullRequests().execute(
                pullRequestList);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchReviewsFromPullRequest(PullRequest pullRequest) {
        try {
            // Fetch Reviews from each Pull Request.
            List<Review> reviewList = new FetchReviewsFromPullRequest(reviewRepository)
                .execute(pullRequest, getRequestDateRange());
            LoggerFactory.getLogger(this.getClass()).info(
                String.format("pr: %s, count: %s", pullRequest.getNumber(), reviewList.size()));
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchCommentsFromPullRequest(PullRequest pullRequest) {
        try {
            // Fetch Comments from each Pull Request.
            List<Comment> commentList = new FetchCommentsFromPullRequest(commentRepository)
                .execute(pullRequest, getRequestDateRange());
            LoggerFactory.getLogger(this.getClass()).info(
                String.format("pr: %s, count: %s", pullRequest.getNumber(), commentList.size()));
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isBetweenRequestRange(Instant instant) {
        return instant.isAfter(userOptionRequest.getFrom().toInstant()) && instant.isBefore(
            userOptionRequest.getTo().toInstant());
    }

    private DateRange getRequestDateRange() {
        return DateRange.builder()
            .from(userOptionRequest.getFrom().toInstant())
            .to(userOptionRequest.getTo().toInstant())
            .build();
    }
}
