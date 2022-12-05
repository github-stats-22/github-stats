package io.pakland.mdas.githubstats.application;

import io.pakland.mdas.githubstats.domain.Comment;
import io.pakland.mdas.githubstats.domain.UserReview;
import io.pakland.mdas.githubstats.domain.UserReviewAggregation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregateUserReviewsTest {

    @Test
    public void aggregatingReviews_shouldGiveValidCommentLengthSum() {
        int sum = 0;
        List<UserReview> reviews = new ArrayList<>();
        Random random = new Random();

        // Generate 10 User reviews, each with random comments
        for (int i = 0; i < 10; i++) {
            List<Comment> comments = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Comment comment = Mockito.mock(Comment.class);
                int randomLength = random.nextInt(10);
                comment.setLength(randomLength);
                comments.add(comment);
                sum += comment.getLength();  // accumulate comment length to assert at the end
            }
            UserReview userReview = Mockito.mock(UserReview.class);
            userReview.setComments(comments);
            reviews.add(userReview);
        }

        int commentLengthSum = UserReviewAggregation.aggregate(reviews).getCommentLengthSum();
        assertEquals(sum, commentLengthSum);
    }
}
