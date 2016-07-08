package me.etki.grac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotated functionality doesn't affect anything yet.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface NotImplemented {
}
