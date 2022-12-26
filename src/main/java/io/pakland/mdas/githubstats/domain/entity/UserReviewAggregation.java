package io.pakland.mdas.githubstats.domain.entity;

import java.util.List;

public class UserReviewAggregation implements CSVExportable {

    private int internalCommentLengthSum = 0;
    private int externalCommentLengthSum = 0;
    private int internalCommentCount = 0;
    private int externalCommentCount = 0;

    public UserReviewAggregation aggregate(List<UserReview> userReviews) {
        resetMetrics();

        userReviews.forEach(userReview -> {
            if (userReview.isInternal()) {
                internalCommentLengthSum += userReview.sumCommentLength();
                internalCommentCount++;
            } else {
                externalCommentLengthSum += userReview.sumCommentLength();
                externalCommentCount++;
            }
        });

        return this;
    }

    private void resetMetrics() {
        internalCommentLengthSum = 0;
        externalCommentLengthSum = 0;
        internalCommentCount = 0;
        externalCommentCount = 0;
    }

    public float getInternalCommentLengthAvg() {
        if (internalCommentCount == 0) return 0;
        return (float) internalCommentLengthSum / internalCommentCount;
    }

    public float getExternalCommentLengthAvg() {
        if (externalCommentCount == 0) return 0;
        return (float) externalCommentLengthSum / externalCommentCount;
    }

    public float getTotalCommentLengthAvg() {
        if (internalCommentCount + externalCommentCount == 0) return 0;
        return (float)
                (internalCommentLengthSum + externalCommentLengthSum) /
                (internalCommentCount + externalCommentCount);
    }

    public int getInternalCommentLengthSum() {
        return internalCommentLengthSum;
    }

    public int getExternalCommentLengthSum() {
        return externalCommentLengthSum;
    }

    public int getInternalCommentCount() {
        return internalCommentCount;
    }

    public int getExternalCommentCount() {
        return externalCommentCount;
    }

    @Override
    public String toCSV() {
        String sep = ",";
        String lineSep = "\n";

        List<String> metrics = List.of("internalCommentLengthAvg", "externalCommentLengthAvg");
        List<Object> data = List.of(getInternalCommentLengthAvg(), getExternalCommentLengthAvg());

        String header = String.join(sep, metrics);
        String body = String.join(sep, data.stream().map(Object::toString).toList());

        return header + lineSep + body;
    }
}
