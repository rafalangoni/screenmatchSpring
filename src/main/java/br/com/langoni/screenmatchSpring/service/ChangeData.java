package br.com.langoni.screenmatchSpring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChangeData implements IChangeData{
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> classGeneric) {
        try {
            return mapper.readValue(json, classGeneric);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
