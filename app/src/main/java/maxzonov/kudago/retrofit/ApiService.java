package maxzonov.kudago.retrofit;

import io.reactivex.Observable;
import maxzonov.kudago.model.ResponseData;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v1.4/events/?fields=id,dates,title,place,price,description,images")
    Observable<ResponseData> getJson(
            @Query("location")
            String city
    );
}
