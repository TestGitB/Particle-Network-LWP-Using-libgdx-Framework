package ark.lwp.minimal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Jayan on 30-07-2017.
 */

public class LiveWallpaperStarter extends Game{

    @Override
    public void create() {
        setScreen(new LiveWallpaperScreen(this));

    }
}
