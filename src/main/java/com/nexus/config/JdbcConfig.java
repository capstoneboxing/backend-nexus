package com.nexus.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    private final ObjectMapper objectMapper;

    public JdbcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @NullMarked
    protected List<?> userConverters() {
        return List.of(
                new TimestampToOffsetDateTimeConverter(),
                new JsonNodeToPgObjectConverter(objectMapper),
                new PgObjectToJsonNodeConverter(objectMapper)
        );
    }

    @ReadingConverter
    static class TimestampToOffsetDateTimeConverter implements Converter<Timestamp, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(Timestamp source) {
            return source.toInstant().atOffset(ZoneOffset.UTC);
        }
    }

    @WritingConverter
    static class JsonNodeToPgObjectConverter implements Converter<JsonNode, PGobject> {

        private final ObjectMapper objectMapper;

        JsonNodeToPgObjectConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public PGobject convert(@NonNull JsonNode source) {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType("jsonb");
                pgObject.setValue(objectMapper.writeValueAsString(source));
                return pgObject;
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to convert JsonNode to jsonb", e);
            }
        }
    }

    @ReadingConverter
    static class PgObjectToJsonNodeConverter implements Converter<PGobject, JsonNode> {

        private final ObjectMapper objectMapper;

        PgObjectToJsonNodeConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public JsonNode convert(@NonNull PGobject source) {
            try {
                if (source.getValue() == null) {
                    return null;
                }
                return objectMapper.readTree(source.getValue());
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to convert jsonb to JsonNode", e);
            }
        }
    }
}