package ark.lwp.minimal;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

public class LiveWallpaperAndroid extends AndroidLiveWallpaperService {

    SettingsPref pref;
    @Override
    public void onCreateApplication () {
        super.onCreateApplication();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //config.useGL20 = false;
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;

        pref=new SettingsPref(getApplicationContext());
        pref.get_size();
        pref.get_velocity();
        pref.get_path();
        pref.get_color();
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
            // TODO Auto-generated constructor stub
        }

        @Override
        public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            //Log.i("LiveWallpaper test", "offsetChange(xOffset:"+xOffset+" yOffset:"+yOffset+" xOffsetSteep:"+xOffsetStep+" yOffsetStep:"+yOffsetStep+" xPixelOffset:"+xPixelOffset+" yPixelOffset:"+yPixelOffset+")");
        }

        @Override
        public void previewStateChange (boolean isPreview) {

            pref.get_size();
            pref.get_velocity();
            pref.get_path();
            pref.get_color();
            //Log.i("LiveWallpaper test", "previewStateChange(isPreview:"+isPreview+")");
        }
    }
}