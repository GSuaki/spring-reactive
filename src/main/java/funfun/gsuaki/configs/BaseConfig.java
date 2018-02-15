package funfun.gsuaki.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Configuration
public class BaseConfig {

    private static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Bean
    public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).build();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT));
        objectMapper.registerModule(addEnumModuleToObjectMapper());
        return objectMapper;
    }

    private SimpleModule addEnumModuleToObjectMapper() {
        final SimpleModule module = new SimpleModule("enum-module");
        module.addSerializer(new StdSerializer<Enum>(Enum.class) {
            @Override
            public void serialize(final Enum value, final JsonGenerator gen,
                                  final SerializerProvider provider) throws IOException {
                gen.writeString(value.name().toLowerCase());
            }
        });
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<Enum> modifyEnumDeserializer(final DeserializationConfig config,
                                                                 final JavaType type,
                                                                 final BeanDescription beanDesc,
                                                                 final JsonDeserializer<?> deserializer) {
                return new JsonDeserializer<Enum>() {
                    @SuppressWarnings("unchecked")
                    public Enum deserialize(JsonParser jp, DeserializationContext context) throws IOException {
                        final Class<? extends Enum> rawClass = (Class<Enum<?>>)type.getRawClass();
                        return Enum.valueOf(rawClass, jp.getValueAsString().toUpperCase());
                    }
                };
            }
        });
        return module;
    }
}
