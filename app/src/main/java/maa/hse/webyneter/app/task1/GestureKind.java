package maa.hse.webyneter.app.task1;

/**
 * Created by webyn on 9/7/2016.
 */
public enum GestureKind {
    onDown("Down"),
    onFling("Fling"),
    onLongPress("Long Press"),
    onScroll("Scroll"),
    onShowPress("Show Press"),
    onSingleTap("Single Tap"),
    onSingleTapUp("Single Tap Up"),
    onDoubleTap("Double Tap");

    private final String value;

    GestureKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}