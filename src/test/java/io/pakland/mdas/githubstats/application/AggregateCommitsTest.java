package io.pakland.mdas.githubstats.application;

import io.pakland.mdas.githubstats.domain.Commit;
import io.pakland.mdas.githubstats.domain.CommitAggregation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class AggregateCommitsTest {

    @Test
    public void aggregatingCommits_shouldGiveValidNumCommits() {
        List<Commit> commits = new ArrayList<>();

        int numCommits = new Random().nextInt(10);

        for (int i = 0; i < numCommits; i++) {
            commits.add(mock(Commit.class));
        }

        CommitAggregation commitAggregation = CommitAggregation.aggregate(commits);
        assertEquals(commitAggregation.getNumCommits(), numCommits);
    }

}
