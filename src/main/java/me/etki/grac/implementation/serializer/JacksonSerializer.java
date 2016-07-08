package me.etki.grac.implementation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.net.MediaType;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.io.SerializationResult;
import me.etki.grac.io.Serializer;
import me.etki.grac.utility.TypeSpec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class JacksonSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonSerializer() {
        this(new ObjectMapper());
    }

    @Override
    public <T> T deserialize(InputStream data, MediaType mimeType, TypeSpec type)
            throws IOException, SerializationException {

        JavaType javaType = convertTypeSpec(type);
        try {
            return objectMapper.readValue(data, javaType);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to deserialize JSON", e);
        }
    }

    @Override
    public <T> SerializationResult serialize(T object, MediaType mimeType) throws IOException, SerializationException {
        byte[] bytes = objectMapper.writeValueAsBytes(object);
        return new SerializationResult()
                .setMimeType(mimeType)
                .setSize((long) bytes.length)
                .setContent(new PushbackInputStream(new ByteArrayInputStream(bytes)));
    }

    @Override
    public boolean supports(MediaType mimeType) {
        return "application".equals(mimeType.type()) &&
                ("json".equals(mimeType.subtype()) || mimeType.subtype().endsWith("+json"));
    }

    private JavaType convertTypeSpec(TypeSpec typeSpec) {
        TypeFactory factory = objectMapper.getTypeFactory();
        if (Map.class.isAssignableFrom(typeSpec.getRootType())) {
            if (typeSpec.getParameters().size() >= 2) {
                JavaType firstParameter = convertTypeSpec(typeSpec.getParameters().get(0));
                JavaType secondParameter = convertTypeSpec(typeSpec.getParameters().get(1));
                return factory.constructMapLikeType(typeSpec.getRootType(), firstParameter, secondParameter);
            }
        } else if (Collection.class.isAssignableFrom(typeSpec.getRootType())) {
            if (!typeSpec.getParameters().isEmpty()) {
                return factory.constructCollectionLikeType(typeSpec.getRootType(),
                        convertTypeSpec(typeSpec.getParameters().get(0)));
            } else {
                return factory.constructRawCollectionLikeType(typeSpec.getRootType());
            }
        } else if (typeSpec.getRootType().isArray()) {
            return factory.constructArrayType(typeSpec.getRootType().getComponentType());
        }
        List<JavaType> parameters = typeSpec.getParameters().stream()
                .map(this::convertTypeSpec)
                .collect(Collectors.toList());
        return factory.constructSimpleType(typeSpec.getRootType(), parameters.toArray(new JavaType[0]));
    }
}
