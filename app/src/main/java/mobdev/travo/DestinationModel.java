package mobdev.travo;

public class DestinationModel {
    private String title;
    private String address;
    private String description;
    private String imageFilename; // Store filename instead of resource ID

    public DestinationModel(String title, String address, String description, String imageFilename) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.imageFilename = imageFilename;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getImageFilename() {
        return imageFilename;
    }
}