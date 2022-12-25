package io.pakland.mdas.githubstats.domain.entity;

import java.util.List;

public class ReviewAggregation implements CSVExportable {

    private int commentLengthSum = 0;
    private int commentCount = 0;

    public ReviewAggregation aggregate(List<Review> reviews) {
        commentCount = reviews.size();
        commentLengthSum = reviews.stream().mapToInt(Review::sumCommentLength).sum();
        return this;
    }

    public float getCommentLengthAvg() {
        if(commentCount == 0) return 0;
        return (float) commentLengthSum / commentCount;
    }

    @Override
    public String toCSV() {
        String sep = ",";
        String lineSep = "\n";

        List<String> metrics = List.of("commentLengthAvg");
        List<Object> data = List.of(getCommentLengthAvg());

        String header = String.join(sep, metrics);
        String body = String.join(sep, data.stream().map(Object::toString).toList());

        return header + lineSep + body;
    }
}