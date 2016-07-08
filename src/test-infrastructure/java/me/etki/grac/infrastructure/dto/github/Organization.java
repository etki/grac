package me.etki.grac.infrastructure.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Organization {

    private String login;
    private Integer id;
    private String url;

    @JsonProperty("repos_url")
    private String reposUrl;
    @JsonProperty("events_url")
    private String eventsUrl;
    @JsonProperty("hooks_url")
    private String hooksUrl;
    @JsonProperty("issues_url")
    private String issuesUrl;
    @JsonProperty("members_url")
    private String membersUrl;
    @JsonProperty("public_members_url")
    private String publicMembersUrl;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String description;

    public String getLogin() {
        return login;
    }

    public Organization setLogin(String login) {
        this.login = login;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Organization setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Organization setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public Organization setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
        return this;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public Organization setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
        return this;
    }

    public String getHooksUrl() {
        return hooksUrl;
    }

    public Organization setHooksUrl(String hooksUrl) {
        this.hooksUrl = hooksUrl;
        return this;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public Organization setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
        return this;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public Organization setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
        return this;
    }

    public String getPublicMembersUrl() {
        return publicMembersUrl;
    }

    public Organization setPublicMembersUrl(String publicMembersUrl) {
        this.publicMembersUrl = publicMembersUrl;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Organization setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Organization setDescription(String description) {
        this.description = description;
        return this;
    }
}
