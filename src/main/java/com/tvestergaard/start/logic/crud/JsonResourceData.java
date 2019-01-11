package com.tvestergaard.start.logic.crud;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Implementation of the {@link ResourceData} interface, that contains json data.
 *
 * @param <R> The type of the resource.
 */
public class JsonResourceData<R> implements ResourceData<R>
{

    /**
     * The json data to convert to an object.
     */
    private final String json;

    /**
     * The class of the resource type.
     */
    private final Class<R> rClass;

    /**
     * The gson instance responsible for generating the resource object.
     */
    private final Gson gson;

    /**
     * Creates a new {@link JsonResourceData}.
     *
     * @param json   The json data to convert to an object.
     * @param rClass The class of the resource type.
     * @param gson   The gson instance responsible for generating the resource object.
     */
    public JsonResourceData(String json, Class<R> rClass, Gson gson)
    {
        this.json = json;
        this.rClass = rClass;
        this.gson = gson;
    }

    @Override public R toResource() throws MalformedResourceDataException
    {
        try {
            return gson.fromJson(json, rClass);
        } catch (JsonParseException e) {
            throw new MalformedResourceDataException("Could not parse json.", e);
        }
    }
}
