package mobdev.travo;

public class CommunityItem {
    private String locationName;
    private float rating;
    private String fare;
    private String route;

    private String startLocation;
    private String endLocation;
    private String transportMode;
    private String travelTime;
    private String travelTips;

    public CommunityItem(
            String locationName,
            float  rating,
            String fare,
            String route,
            String startLocation,
            String endLocation,
            String transportMode,
            String travelTime,
            String travelTips
    ) {
        this.locationName   = locationName;
        this.rating         = rating;
        this.fare           = fare;
        this.route          = route;
        this.startLocation  = startLocation;
        this.endLocation    = endLocation;
        this.transportMode  = transportMode;
        this.travelTime     = travelTime;
        this.travelTips     = travelTips;
    }

    public String getLocationName() { return locationName; }
    public float getRating() { return rating; }
    public String getFare() { return fare; }
    public String getRoute() { return route; }

    public String getStartLocation()      { return startLocation; }
    public String getEndLocation()        { return endLocation; }
    public String getTransportMode()      { return transportMode; }
    public String getTravelTime()         { return travelTime; }
    public String getTravelTips()         { return travelTips; }
}
