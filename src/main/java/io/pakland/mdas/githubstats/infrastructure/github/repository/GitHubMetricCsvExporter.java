package io.pakland.mdas.githubstats.infrastructure.github.repository;

import io.pakland.mdas.githubstats.domain.repository.FileMetricExporter;
import io.pakland.mdas.githubstats.domain.Metric;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitHubMetricCsvExporter implements FileMetricExporter {

    @Override
    public void export(List<Metric> metrics, String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> headerFields = new ArrayList<>(Arrays.asList(
            "organization", "team_slug", "user_name", "total_pulls", "merged_pulls",
            "internal_reviews", "external_reviews", "comments_avg_length",
            "commits_count", "lines_added", "lines_removed", "from", "to"));

        sb.append(this.appendStringArrayList(headerFields));
        metrics.forEach(metric ->
            sb.append(this.appendStringArrayList(metric.getValuesAsStringArrayList())));

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(sb.toString());
        }
    }

    private String appendStringArrayList(ArrayList<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            sb.append(fields.get(i));
            if (i < fields.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
