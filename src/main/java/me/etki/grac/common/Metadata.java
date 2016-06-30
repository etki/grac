package me.etki.grac.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Metadata extends Map<String, List<String>> {

    default void addEntry(String key, String value) {
        synchronized (this) {
            List<String> values = get(key);
            if (values == null) {
                values = new ArrayList<>();
                put(key, values);
            }
            values.add(value);
        }
    }
}
