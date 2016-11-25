package com.example;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class EmptyRequestBodyConverterFactory extends Converter.Factory {

  private static final Converter<?, RequestBody> CONVERTER = new Converter<Object, RequestBody>() {
    @Override
    public RequestBody convert(Object value) throws IOException {
      return EmptyOutput.INSTANCE;
    }
  };

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (Utils.isAnnotationPresent(methodAnnotations, EmptyBody.class)) {
      return CONVERTER;
    } else {
      return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
  }

  public static Converter.Factory create() {
    return new EmptyRequestBodyConverterFactory();
  }
}
