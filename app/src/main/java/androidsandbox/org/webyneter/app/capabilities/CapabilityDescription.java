package androidsandbox.org.webyneter.app.capabilities;

/**
 * Created by webyn on 10/7/2016.
 */

public final class CapabilityDescription {
    private final String name;
    private final int imageResourceId;
    private final String description;

    public CapabilityDescription(String name, int imageResourceId, String description) {
        this.description = description;
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }
}
