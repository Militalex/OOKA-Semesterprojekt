package com.github.militalex.messaging.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;

public class PairDeserializer<F, S> implements Deserializer<Pair<F, S>> {
    private final Class<F> firstClass;
    private final Class<S> secondClass;

    public PairDeserializer(Class<F> firstClass, Class<S> secondClass) {
        this.firstClass = firstClass;
        this.secondClass = secondClass;
    }

    @Override
    public Pair<F, S> deserialize(String topic, byte[] bytes) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type pairType = new TypeToken<Pair<String, String>>() {}.getType();
        gsonBuilder.registerTypeAdapter(pairType, new PairGsonDeserializer());

        Gson gson = gsonBuilder.create();
        Pair<String, String> stringPair = gson.fromJson(new String(bytes), new TypeToken<>() {});
        return new Pair<>(
                gson.fromJson(stringPair.getFirst(), firstClass),
                gson.fromJson(stringPair.getSecond(), secondClass)
        );

    }

    private static class PairGsonDeserializer implements JsonDeserializer<Pair<String, String>> {

        @Override
        public Pair<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String first = jsonObject.get("first").toString();
            String second = jsonObject.get("second").toString();

            return new Pair<>(first, second);
        }
    }
}
