package maxzonov.kudago.model.main.place;

import com.google.gson.annotations.SerializedName;

public class PlaceDetail {

    @SerializedName("title")
    private String title;

    @SerializedName("address")
    private String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
