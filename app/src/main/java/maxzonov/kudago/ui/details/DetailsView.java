package maxzonov.kudago.ui.details;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

public interface DetailsView extends MvpView {
    void navigateToGoogleMaps(ArrayList<String> coords);
}
