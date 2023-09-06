package com.codecademy.diningreviewapi.serializer;

import java.io.IOException;

import com.codecademy.diningreviewapi.enums.ReviewStatus;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ReviewDeserializer extends JsonDeserializer<ReviewStatus>
{
	@Override
	public ReviewStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
		
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        if (node == null) {
            return null;
        }

        String text = node.textValue();

        if (text == null) {
            return null;
        }

        return ReviewStatus.valueOf(text);
	}
}