package com.example.secureapi.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;


@Component
public class HtmlSanitizerSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Экранируем HTML-символы в строке
            String sanitizedValue = HtmlUtils.htmlEscape(value);
            gen.writeString(sanitizedValue);
        } else {
            gen.writeNull();
        }
    }
}