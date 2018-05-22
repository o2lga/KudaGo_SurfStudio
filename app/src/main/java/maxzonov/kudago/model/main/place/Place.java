package maxzonov.kudago.model.main.place;

import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("coords")
    private Coords coords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    class Coords {

        @SerializedName("lat")
        private String latitude;

        @SerializedName("lon")
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
