package org.thraex.toolkit.configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author 鬼王
 * @date 2021/08/30 16:10
 */
public class TemporalFormatConfiguration {

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration#jacksonObjectMapper}
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern(FORMAT_DATE);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(FORMAT_TIME);
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);

        return builder -> builder
                .serializers(new LocalDateSerializer(date),
                        new LocalTimeSerializer(time),
                        new LocalDateTimeSerializer(dateTime))
                .deserializers(new LocalDateDeserializer(date),
                        new LocalTimeDeserializer(time),
                        new LocalDateTimeDeserializer(dateTime));
    }

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer.class)
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizerV1() {
        final int capacity = 3;
        DateTimeFormatter date = DateTimeFormatter.ofPattern(FORMAT_DATE);
        DateTimeFormatter time = DateTimeFormatter.ofPattern(FORMAT_TIME);
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);

        return builder -> builder
                .serializersByType(new HashMap<>(capacity) {{
                    put(LocalDate.class, new LocalDateSerializer(date));
                    put(LocalTime.class, new LocalTimeSerializer(time));
                    put(LocalDateTime.class, new LocalDateTimeSerializer(dateTime));
                }})
                .deserializersByType(new HashMap<>(capacity) {{
                    put(LocalDate.class, new LocalDateDeserializer(date));
                    put(LocalTime.class, new LocalTimeDeserializer(time));
                    put(LocalDateTime.class, new LocalDateTimeDeserializer(dateTime));
                }});
    }

}
