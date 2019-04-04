package com.hieugie.banthe.service.mapper;



import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonParser;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.ObjectCodec;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.DeserializationContext;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author dungvm
 */
public class JsonDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();
        TextNode node = (TextNode) oc.readTree(jp);
        String dateString = node.textValue();

        Instant instant = Instant.parse(dateString);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
        return date;
    }
}
