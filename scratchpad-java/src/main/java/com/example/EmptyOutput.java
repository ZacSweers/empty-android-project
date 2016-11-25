package com.example;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

class EmptyOutput extends RequestBody {

  private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

  public static final RequestBody INSTANCE = new EmptyOutput();

  private EmptyOutput() {
  }

  @Override
  public MediaType contentType() {
    return MEDIA_TYPE;
  }

  @Override
  public long contentLength() {
    return 0;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException { }
}
