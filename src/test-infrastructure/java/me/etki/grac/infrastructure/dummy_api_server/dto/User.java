package me.etki.grac.infrastructure.dummy_api_server.dto;

import java.util.UUID;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class User {
    private UUID id;
    private String login;

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }
}
