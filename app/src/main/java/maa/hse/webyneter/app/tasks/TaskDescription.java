package maa.hse.webyneter.app.tasks;

/**
 * Created by webyn on 10/7/2016.
 */

public final class TaskDescription {
    private final String name;
    private final int imageResourceId;
    private final String description;

    public TaskDescription(String name, int imageResourceId, String description) {
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
