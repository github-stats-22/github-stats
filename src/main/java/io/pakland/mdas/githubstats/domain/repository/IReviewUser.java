package io.pakland.mdas.githubstats.domain.repository;

import io.pakland.mdas.githubstats.domain.ReviewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Add jdoc about the rep
 */

@Repository
public interface IReviewUser extends JpaRepository<ReviewUser,Integer> {
}
