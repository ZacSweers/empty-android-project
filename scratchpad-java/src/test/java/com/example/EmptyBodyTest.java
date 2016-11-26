package com.example;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.POST;

import static com.google.common.truth.Truth.assertThat;

public class EmptyBodyTest {

  @Rule public MockWebServer server = new MockWebServer();

  Retrofit retrofit;

  @Before
  public void setUp() {
    OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.body().contentLength() == 0) {
          request = request.newBuilder().post(EmptyOutput.INSTANCE).build();
        }
        return chain.proceed(request);
      }
    }).build();

    retrofit = new Retrofit.Builder().client(client)
        .baseUrl(server.url("/")) // Local Server: "http://localhost:1234"
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
  }

  public interface SampleApi {
    @POST("empty")
    Call<Thing> emptyPost();
  }

  public static class Thing {
    public int a;
    public int b;
  }

  @Test
  public void empty() throws Exception {
    server.enqueue(new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
        .setBody("{" + "\"a\": 4," + "\"b\": 6" + "}"));

    SampleApi service = retrofit.create(SampleApi.class);

    Response<Thing> response = service.emptyPost().execute();

    RecordedRequest request = server.takeRequest();
    assertThat(request.getBodySize()).isEqualTo(2);
    assertThat(request.getBody().readByteString().utf8()).isEqualTo("{}");
  }
}
