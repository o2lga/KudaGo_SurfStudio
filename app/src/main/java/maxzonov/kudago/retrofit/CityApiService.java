package maxzonov.kudago.retrofit;

import java.util.ArrayList;

import io.reactivex.Observable;
import maxzonov.kudago.model.City;
import retrofit2.http.GET;

public interface CityApiService {

    @GET("v1.4//locations/")
    Observable<ArrayList<City>> getCityJson();
}
