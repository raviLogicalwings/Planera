package com.planera.mis.planera2.activities.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StringConverterFactory extends Converter.Factory {

    private StringConverterFactory() {}

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return (Converter<ResponseBody, String>) value -> value.string();
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                    Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return (Converter<String, RequestBody>) value -> RequestBody.create(MediaType.parse("text/plain"), value);
    }
}