package maa.hse.webyneter.app;

/**
 * Created by webyn on 10/7/2016.
 */

public final class TaskDescription {
    private final String name;
    private final int screenshotResourceId;
    private final String description;

    public TaskDescription(String name, int imageResourceId, String description) {
        this.description = description;
        this.name = name;
        this.screenshotResourceId = imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public int getScreenshotResourceId() {
        return screenshotResourceId;
    }

    public String getName() {
        return name;
    }
}
