package maxzonov.kudago.retrofit;

import io.reactivex.Observable;
import maxzonov.kudago.model.ResponseData;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v1.4/events/?fields=id,dates,title,place,price,description,images,body_text")
    Observable<ResponseData> getJson(
            @Query("location")
            String city
    );
}
