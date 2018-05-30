package maxzonov.kudago.model;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("slug")
    private String slug;

    @SerializedName("name")
    private String name;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
