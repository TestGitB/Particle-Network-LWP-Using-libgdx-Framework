package ark.lwp.minimalfree;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class LiveWallpaperAndroid extends AndroidLiveWallpaperService {

    SettingsPref pref;
    @Override
    public void onCreateApplication () {
        super.onCreateApplication();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.numSamples=2;
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;

        pref=new SettingsPref(getApplicationContext());
        pref.get_thickness();
        pref.get_size();
        pref.get_velocity();
        pref.get_path();
        pref.get_color();
        pref.get_back_color();
        pref.get_radius();
        pref.get_line_color();
        pref.get_touch();
        pref.get_line_length();
        ApplicationListener listener = new LiveWallpaperStarter();
        initialize(listener, config);


    }

    public class MyLiveWallpaperListener extends LiveWallpaperScreen implements AndroidWallpaperListener {
        public MyLiveWallpaperListener(Game game) {
            super(game);
            pref.get_size();
            pref.get_velocity();
            pref.get_path();
            pref.get_color();
            pref.get_back_color();
            pref.get_radius();
            pref.get_line_color();
            pref.get_touch();
            pref.get_line_length();
            pref.get_thickness();
            // TODO Auto-generated constructor stub
        }

        @Override
        public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            Log.d("LiveWallpaper test", "offsetChange(xOffset:"+xOffset+" yOffset:"+yOffset+" xOffsetSteep:"+xOffsetStep+" yOffsetStep:"+yOffsetStep+" xPixelOffset:"+xPixelOffset+" yPixelOffset:"+yPixelOffset+")");


        }

        @Override
        public void previewStateChange (boolean isPreview) {

            pref.get_size();
            pref.get_velocity();
            pref.get_path();
            pref.get_color();
            pref.get_back_color();
            pref.get_radius();
            pref.get_line_color();
            pref.get_touch();
            pref.get_line_length();
            pref.get_thickness();
            //Log.i("LiveWallpaper test", "previewStateChange(isPreview:"+isPreview+")");
        }
    }
}