package ark.lwp.minimal;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Map;

public class MainActivity extends AppCompatActivity{

    EditText t;
    Button b;
    SettingsPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref =new SettingsPref(getApplicationContext());


        t=(EditText)findViewById(R.id.edittext);
        b=(Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x=Integer.parseInt(t.getText().toString());
             //   pref.putInteger("size",x);
               // p.putInt("size",x);

                pref.set_size(x);
                Intent i=new Intent(MainActivity.this,LiveWallpaperAndroid.class);
                i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                String pkg = WallpaperService.class.getPackage().getName();
                String cls = WallpaperService.class.getCanonicalName();
                i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(pkg, cls));
                startService(i);
            }
        });

    }
}
