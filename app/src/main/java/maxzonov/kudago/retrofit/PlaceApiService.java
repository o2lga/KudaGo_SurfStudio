package maxzonov.kudago.retrofit;

import maxzonov.kudago.model.main.place.PlaceDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlaceApiService {
    @GET("v1.4/places/{place}/?fields=title,address")
    Call<PlaceDetail> getPlaceJson(
            @Path("place")
                    String place
    );
}
