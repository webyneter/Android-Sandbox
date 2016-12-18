package androidsandbox.org.webyneter.app.util;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;

import androidsandbox.org.webyneter.app.AndroidSandboxApplication;

/**
 * Created by webyn on 12/8/2016.
 */

public final class TrackerHelper {
    public static void sendWithDefaultTracker(Activity activity, String action) {
        ((AndroidSandboxApplication) activity.getApplication()).getDefaultTracker()
                .send(new HitBuilders.EventBuilder()
                        .setCategory(activity.getClass().getSimpleName())
                        .setAction(action)
                        .build());
    }
}
