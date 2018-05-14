package maxzonov.kudago.model.main.image;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("image")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
