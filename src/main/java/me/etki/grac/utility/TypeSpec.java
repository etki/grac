package me.etki.grac.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TypeSpec {

    private final Class rootType;
    private final List<TypeSpec> parameters;

    public TypeSpec(Class rootType, List<TypeSpec> parameters) {
        this.rootType = rootType;
        this.parameters = parameters;
    }

    public TypeSpec(Class rootType, TypeSpec... parameters) {
        this(rootType, Arrays.asList(parameters));
    }

    public TypeSpec(Class rootType) {
        this(rootType, Collections.emptyList());
    }

    public Class getRootType() {
        return rootType;
    }

    public List<TypeSpec> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootType, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (rootType == null || !(obj instanceof TypeSpec)) {
            return false;
        }
        TypeSpec that = (TypeSpec) obj;
        return rootType.equals(that.rootType) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public String toString() {
        return "TypeSpec {root type=`" + (rootType == null ? null : rootType.getName()) + "`, " +
                "parameters=" + parameters + "}";
    }
}
