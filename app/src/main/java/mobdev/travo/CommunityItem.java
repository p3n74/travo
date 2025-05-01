package mobdev.travo;

public class CommunityItem {
    private String locationName;
    private float rating;
    private String fare;
    private String route;

    public CommunityItem(String locationName, float rating, String fare, String route) {
        this.locationName = locationName;
        this.rating = rating;
        this.fare = fare;
        this.route = route;
    }

    public String getLocationName() { return locationName; }
    public float getRating() { return rating; }
    public String getFare() { return fare; }
    public String getRoute() { return route; }
}
