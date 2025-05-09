package mobdev.travo;

public class DestinationModel {
    private String title;
    private String address;
    private int imageResId;

    public DestinationModel(String title, String address, int imageResId) {
        this.title = title;
        this.address = address;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public int getImageResId() {
        return imageResId;
    }
}
