package io.pakland.mdas.githubstats;

import io.pakland.mdas.githubstats.application.exceptions.HttpException;
import io.pakland.mdas.githubstats.domain.User;
import io.pakland.mdas.githubstats.domain.repository.UserExternalRepository;

import java.util.List;

public class FetchUsersFromTeam {
    private UserExternalRepository userExternalRepository;


    public FetchUsersFromTeam(UserExternalRepository userExternalRepository) {
        this.userExternalRepository = userExternalRepository;
    }

     public List<User> execute(String organizationName, String teamName) throws HttpException {
        return this.userExternalRepository.fetchUsersFromTeam(organizationName, teamName);
     }
}
