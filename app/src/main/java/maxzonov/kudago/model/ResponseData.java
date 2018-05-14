package maxzonov.kudago.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import maxzonov.kudago.model.main.Event;

public class ResponseData {

    @SerializedName("results")
    private ArrayList<Event> events;

    @SerializedName("next")
    private String nextPage;

    @SerializedName("previous")
    private String previousPage;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }
}
