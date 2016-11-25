package com.example;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.google.common.truth.Truth.assertThat;

public class EmptyBodyTest {

  @Rule public MockWebServer server = new MockWebServer();

  Retrofit retrofit;

  @Before
  public void setUp() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(logging).build();

    retrofit = new Retrofit.Builder().client(client)
        .baseUrl(server.url("/")) // Local Server: "http://localhost:1234"
        .addConverterFactory(EmptyRequestBodyConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
  }

  public interface SampleApi {
    @POST("empty")
    @EmptyBody
    Call<Thing> emptyPost(@Body String blerg);  // Has to have a body param in order for converter factory to be hit
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

    Response<Thing> response = service.emptyPost("hi").execute();

    RecordedRequest request = server.takeRequest();
    assertThat(request.getBody().readByteString().utf8()).isEmpty();
  }
}
