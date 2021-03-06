package ark.lwp.minimalfree;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaredrummler.android.colorpicker.ColorPreference;
import com.pavelsikun.seekbarpreference.SeekBarPreference;


import static java.lang.Math.min;

public class SettingsPrefActivity extends AppCompatActivity {

    static int PICK_IMAGE=1;
    static SettingsPref myPref;
    static int width;
    static int height;
    private static AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pref);

        MobileAds.initialize(this, "ca-app-pub-3690357492073975~5025810258");
        adView=(AdView)findViewById(R.id.adView9);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        getSupportActionBar();
        myPref=new SettingsPref(getApplication());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height=displayMetrics.heightPixels;
        width=min(width,height);

        getFragmentManager().beginTransaction().replace(R.id.layoutlin,new MainPreferenceFragment()).commit();
                //.replace(android.R.id., new MainPreferenceFragment()).commit();

    }
    public static class MainPreferenceFragment extends PreferenceFragment{
        Preference bgImage;
        SeekBarPreference length;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            Preference lColorPref=findPreference("line_color");
            lColorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    String hex_color=String.format("#%06X", (0xFFFFFF & (int)newValue));

                    hex_color=hex_color.concat("FF");

                    myPref.set_line_color(hex_color);
                    return true;
                }
            });


            final SeekBarPreference pCount=(SeekBarPreference) findPreference("particle_count");
            pCount.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("Pref chanfe",String.valueOf(newValue));
                    myPref.set_size((int)newValue);
                    return true;
                }
            });

            length=(SeekBarPreference) findPreference("line_length");
            length.setMaxValue(width);
            length.setCurrentValue(myPref.get_line_length());
            length.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("Pref chanfe",String.valueOf(newValue));
                    myPref.set_line_length((int)newValue);
                    return true;
                }
            });

            final SeekBarPreference pVel=(SeekBarPreference)findPreference("particle_velocity");
            pVel.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    myPref.set_velocity((int)newValue);
                    return true;
                }
            });
            final ColorPreference pColorPref = (ColorPreference) findPreference("particle_color");
            pColorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("CHenged",String.valueOf(newValue));

                    String hex_color=String.format("#%06X", (0xFFFFFF & (int)newValue));

                    hex_color=hex_color.concat("FF");

                    Log.d("CCOlr",hex_color);
                    myPref.set_color(hex_color);
                    //FREE VERSION
                    myPref.set_line_color(hex_color);
                    return true;
                }
            });
            final SeekBarPreference pRad=(SeekBarPreference)findPreference("particle_size") ;

            final SeekBarPreference thick=(SeekBarPreference)findPreference("thick");

            Preference pSave=findPreference("save");
            pSave.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    myPref.set_velocity(pVel.getCurrentValue());
                    Log.d("velocity in pref",String.valueOf(pVel.getCurrentValue()));
                    myPref.set_size(pCount.getCurrentValue());
                    myPref.set_radius(pRad.getCurrentValue());
                    myPref.set_line_length(length.getCurrentValue());
                    myPref.set_thickness(thick.getCurrentValue());
                    Intent intent = new Intent();
                    String pkg = LiveWallpaperAndroid.class.getPackage().getName();
                    String cls = LiveWallpaperAndroid.class.getCanonicalName();
                    intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(pkg, cls));

                    startActivity(intent);

                    return true;
                }
            });

            bgImage=findPreference("image_bg");

            bgImage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    startActivityForResult(chooserIntent,PICK_IMAGE);
                    return true;
                }
            });


            final ColorPreference colorBg = (ColorPreference) findPreference("color_bg");

            colorBg.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("CHenged",String.valueOf(newValue));

                    String hex_color=String.format("#%06X", (0xFFFFFF & (int)newValue));

                    hex_color=hex_color.concat("FF");

                    myPref.set_back_color(hex_color);

                    bgImage.setSummary("Select background image from gallery.");
                    myPref.set_path("0");
                    return true;
                }
            });

            SwitchPreference tEffect=(SwitchPreference)findPreference("touch_effect");
            tEffect.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("Checked",String.valueOf(newValue));
                        if((Boolean)newValue)
                            myPref.set_touch(1);
                        else myPref.set_touch(0);
                    return true;
                }
            });


        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE && resultCode==RESULT_OK) {
                //TODO: action
                Log.d("Intent data: " ,data.toString());
                String s=getRealPathFromURI(data.getData());
                s=s.replace(Environment.getExternalStorageDirectory().toString(),"");
                myPref.set_path(s);

                bgImage.setSummary("Selected image: "+SettingsPref.path);
                Log.d("Intent s: " ,s);
            }
        }

        private String getRealPathFromURI(Uri contentUri) {
            String[] proj = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }


    }

/*    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(getApplication(),"About Selected",Toast.LENGTH_LONG).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   */
}