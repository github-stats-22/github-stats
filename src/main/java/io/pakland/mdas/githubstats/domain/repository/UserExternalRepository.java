package io.pakland.mdas.githubstats.domain.repository;

import io.pakland.mdas.githubstats.application.dto.UserDTO;
import io.pakland.mdas.githubstats.application.exceptions.HttpException;

import java.util.List;

public interface UserExternalRepository {
    List<UserDTO> fetchUsersFromTeam(Integer organizationId, Integer teamId) throws HttpException;
}
