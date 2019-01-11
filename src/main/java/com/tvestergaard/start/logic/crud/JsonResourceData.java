package com.tvestergaard.start.logic.crud;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonResourceData<T> implements ResourceData<T>
{

    private final String   json;
    private final Class<T> tClass;
    private final Gson     gson;

    public JsonResourceData(String json, Class<T> tClass, Gson gson)
    {
        this.json = json;
        this.tClass = tClass;
        this.gson = gson;
    }

    @Override public T toEntity() throws MalformedResourceDataException
    {
        try {
            return gson.fromJson(json, tClass);
        } catch (JsonParseException e) {
            throw new MalformedResourceDataException("Could not parse json.", e);
        }
    }
}
