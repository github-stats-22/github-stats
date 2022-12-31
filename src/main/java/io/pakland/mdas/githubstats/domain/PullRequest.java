package io.pakland.mdas.githubstats.domain;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequest {

    private Integer id;

    private Integer number;

    private PullRequestState state;

    private boolean merged;

    private Instant closedAt;

    private Instant createdAt;

    private Integer numCommits;

    private Integer additions;

    private Integer deletions;

    @ToString.Exclude
    private Repository repository;

    private User user;

    public boolean isMerged() {
        return this.merged;
    }

    public boolean isAuthorNamed(String name) {
        return this.user.isNamed(name);
    }
}
