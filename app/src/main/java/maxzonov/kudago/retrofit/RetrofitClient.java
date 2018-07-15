package maxzonov.kudago.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://kudago.com/public-api/v1.4/";
    private static OkHttpClient client = new OkHttpClient();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    public static EventApiService getApiService() {
        return getRetrofitInstance().create(EventApiService.class);
    }

    public static CityApiService getCityApiService() {
        return getRetrofitInstance().create(CityApiService.class);
    }
}
