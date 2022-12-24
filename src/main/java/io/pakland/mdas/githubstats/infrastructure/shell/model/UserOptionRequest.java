package io.pakland.mdas.githubstats.infrastructure.shell.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Builder
public class UserOptionRequest {

    private String userName;

    private String apiKey;

    private Date from;

    private Date to;
}
