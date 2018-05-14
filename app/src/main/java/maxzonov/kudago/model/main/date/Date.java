package maxzonov.kudago.model.main.date;

import com.google.gson.annotations.SerializedName;

public class Date {

    @SerializedName("start")
    private String dateStart;

    @SerializedName("end")
    private String dateEnd;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
