package maxzonov.kudago.model.detail;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailEvent {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("body_text")
    private String fullDescription;

    @SerializedName("price")
    private String price;

    @SerializedName("images")
    private ArrayList<ImageDetail> imagesDetails;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<ImageDetail> getImagesDetails() {
        return imagesDetails;
    }

    public void setImagesDetails(ArrayList<ImageDetail> imagesDetails) {
        this.imagesDetails = imagesDetails;
    }

    public class ImageDetail {

        @SerializedName("image")
        String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
