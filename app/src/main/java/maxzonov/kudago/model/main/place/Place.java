package maxzonov.kudago.model.main.place;

import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
