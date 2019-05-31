package com.moksha.raspberrypi.server.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by sanjay.rajput on 17/11/15.
 */

public class MyObjectMapper {

    private static final Logger logger = LoggerFactory.getLogger(MyObjectMapper.class);

    public MyObjectMapper() {
    }

    public static ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    public static ObjectMapper getTransientDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    public static String getJsonString(Object object) throws JsonProcessingException {
        if (object != null)
            return getDefaultMapper().writeValueAsString(object);
        return null;
    }


    public static String getJsonStringNoException(Object object) {
        try {
            if (object != null)
                return getDefaultMapper().writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Failed to convert object to json", e);
        }
        return null;
    }

    public static <T> T getClassObject(String jsonString, Class<T> valueType) throws Exception {
        try {
            return getDefaultMapper().readValue(jsonString, valueType);
        } catch (JsonParseException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static <T> T getClassObject(String jsonString, TypeReference type) throws Exception {
        try {
            return getDefaultMapper().readValue(jsonString, type);
        } catch (JsonParseException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    private static class IgnoreInheritedAttributes extends JacksonAnnotationIntrospector {

        private Class aClass;

        private IgnoreInheritedAttributes(Class aClass) {
            this.aClass = aClass;
        }

        @Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            return m.getDeclaringClass() == aClass || super.hasIgnoreMarker(m);
        }
    }

    public static ObjectMapper getIgnoreInheritedMapper(Class aclass) {
        ObjectMapper mapper = getDefaultMapper();
        mapper.setAnnotationIntrospector(new IgnoreInheritedAttributes(aclass));
        return mapper;
    }

    public static JsonNode extractNodeAt(String agentJsonString, String path) {
        try {
            JsonNode root = getDefaultMapper().readTree(agentJsonString);
            if (path.startsWith(".")) {
                return root.at(path.replace(".", "/"));
            } else {
                return root.at("/" + path.replace(".", "/"));
            }
        } catch (IOException e) {
            logger.error("failed to read tree: " + e.getMessage(), e);
        }
        return null;
    }

    public static JsonNode getJsonFromString(String jsonString) {
        if (jsonString == null) return null;
        JsonFactory factory = getDefaultMapper().getFactory();
        try {
            JsonParser parser = factory.createParser(jsonString);
            JsonNode node = getDefaultMapper().readTree(parser);
            return node;
        } catch (IOException e) {
            logger.error(" error in parsing Json");
            return null;

        }
    }
}