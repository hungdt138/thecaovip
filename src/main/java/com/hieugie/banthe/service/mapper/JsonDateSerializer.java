package com.hieugie.banthe.service.mapper;



import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author dungvm
 */
public class JsonDateSerializer extends JsonSerializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(LocalDate date, JsonGenerator generator,
                          SerializerProvider provider) throws IOException,
        JsonProcessingException {

        String dateString = date.format(formatter);
        generator.writeString(dateString);
    }
}
