package me.etki.grac.infrastructure.dummy_api_server.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class JsonProblem {

    private String type;
    private String title;
    private String detail;
    private Integer status;
    private String instance;
    private final Map<String, Object> extension = new HashMap<>();

    public String getType() {
        return type;
    }

    public JsonProblem setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public JsonProblem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public JsonProblem setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JsonProblem setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public JsonProblem setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public Map<String, Object> getExtension() {
        return extension;
    }

    public JsonProblem addExtension(String key, Object value) {
        extension.put(key, value);
        return this;
    }
}
