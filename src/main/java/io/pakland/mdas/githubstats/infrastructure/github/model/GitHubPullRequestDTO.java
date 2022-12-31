package io.pakland.mdas.githubstats.infrastructure.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.pakland.mdas.githubstats.application.dto.PullRequestDTO;
import io.pakland.mdas.githubstats.application.dto.UserDTO;
import io.pakland.mdas.githubstats.domain.PullRequestState;

import java.time.Instant;

public class GitHubPullRequestDTO implements PullRequestDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("number")
    private Integer number;

    @JsonProperty("state")
    private GitHubPullRequestStateDTO state;

    @JsonProperty("merged_at")
    private String mergedTimestamp;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("closed_at")
    private Instant closedAt;

    @JsonProperty("commits")
    private Integer numCommits;

    @JsonProperty("additions")
    private Integer additions;

    @JsonProperty("deletions")
    private Integer deletions;

    @JsonProperty("user")
    private GitHubUserDTO user;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public Integer getNumber() {
        return this.number;
    }

    @Override
    public PullRequestState getState() {
        return this.state.getValue();
    }

    @Override
    public boolean getMerged() {
        return this.mergedTimestamp != null;
    }

    @Override
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Instant getClosedAt() {
        return this.closedAt;
    }

    @Override
    public Integer getNumCommits() {
        return this.numCommits;
    }

    @Override
    public Integer getAdditions() {
        return this.additions;
    }

    @Override
    public Integer getDeletions() {
        return this.deletions;
    }

    @Override
    public UserDTO getUser() {
        return this.user;
    }
}
