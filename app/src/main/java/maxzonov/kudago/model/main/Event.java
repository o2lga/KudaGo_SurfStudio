package maxzonov.kudago.model.main;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import maxzonov.kudago.model.main.date.Date;
import maxzonov.kudago.model.main.image.Image;
import maxzonov.kudago.model.main.place.Place;

public class Event {

    @SerializedName("id")
    private int id;

    @SerializedName("images")
    private ArrayList<Image> images;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("place")
    private Place place;

    @SerializedName("dates")
    private ArrayList<Date> dates;

    @SerializedName("price")
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
