package io.pakland.mdas.githubstats.infrastructure.github.repository;

import io.pakland.mdas.githubstats.application.exceptions.HttpException;
import io.pakland.mdas.githubstats.application.mappers.ReviewMapper;
import io.pakland.mdas.githubstats.domain.PullRequest;
import io.pakland.mdas.githubstats.domain.Review;
import io.pakland.mdas.githubstats.domain.repository.ReviewExternalRepository;
import io.pakland.mdas.githubstats.domain.utils.InternalCaching;
import io.pakland.mdas.githubstats.infrastructure.github.model.GitHubPageableRequest;
import io.pakland.mdas.githubstats.infrastructure.github.model.GitHubReviewDTO;
import java.util.List;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ReviewGitHubRepository implements ReviewExternalRepository {

    private final WebClientConfiguration webClientConfiguration;

    private final InternalCaching<String, List<Review>> cache = new InternalCaching<>();

    public ReviewGitHubRepository(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    @Override
    public List<Review> fetchReviewsFromPullRequestByPage(PullRequest pullRequest, Integer page)
        throws HttpException {
        final String uri = String.format("/repos/%s/%s/pulls/%s/reviews?%s",
            pullRequest.getRepository().getOwnerLogin(),
            pullRequest.getRepository().getName(),
            pullRequest.getNumber(),
            new GitHubPageableRequest(page, 100).getRequestUriWithParameters()
        );

        List<Review> maybeResult = cache.get(uri);
        if (maybeResult != null) {
            return maybeResult;
        }

        try {
            List<Review> result = this.webClientConfiguration.getWebClient().get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GitHubReviewDTO.class)
                .parallel()
                .filter(review -> Objects.nonNull(review.getSubmittedAt()) && Objects.nonNull(
                    review.getUser()))
                .map(ReviewMapper::dtoToEntity)
                .sequential()
                .collectList()
                .block();
            cache.add(uri, result);

            return result;
        } catch (WebClientResponseException ex) {
            System.err.println(ex);
            throw new HttpException(ex.getRawStatusCode(), ex.getMessage());
        }
    }
}
