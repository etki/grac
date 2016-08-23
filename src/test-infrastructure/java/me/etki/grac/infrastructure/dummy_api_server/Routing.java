package me.etki.grac.infrastructure.dummy_api_server;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Routing {

    public static final String METHOD_RESOURCE = "/http/method";

    // + {milliseconds}
    public static final String TIMEOUT_RESOURCE = "/api/timeout/";

    public static final String NULL_RESOURCE = "/api/null";

    // + {status code}
    public static final String JSON_PROBLEM_RESOURCE = "/api/error/";

    public static final String USERS_RESOURCE = "/api/structure/user";

    public static final String PAYLOAD_MIRROR_RESOURCE = "/api/payload/mirror";
}
