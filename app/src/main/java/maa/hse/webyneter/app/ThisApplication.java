package maa.hse.webyneter.app;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;

/**
 * Created by webyn on 10/7/2016.
 */

public final class ThisApplication extends Application {
    private static final String API_KEY = "d107818e-892f-40de-8206-de0dd1928bfd";

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetrica.activate(getApplicationContext(), API_KEY);
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
