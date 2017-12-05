package ark.lwp.minimal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.ColorInt;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity{

    EditText t;
    Button b;
    Button colorb;
    Button imgButton;
    SettingsPref pref;
    SeekBar seekBar;
    static int PICK_IMAGE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref =new SettingsPref(getApplicationContext());

        seekBar =(SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress(pref.get_velocity()/6);
        t=(EditText)findViewById(R.id.edittext);
        t.setText(String.valueOf(pref.get_size()));
        b=(Button)findViewById(R.id.button);
        colorb=(Button)findViewById(R.id.colorb);
       // colorb.setBackgroundColor(pref.get_color());
        colorb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ColorPicker cp = new ColorPicker(MainActivity.this, 0, 0, 0);

                /* Show color picker dialog */
                cp.show();

    /* Set a new Listener called when user click "select" */
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        // Do whatever you want
                        // Examples
                        colorb.setBackgroundColor(color);
                        //color= com.badlogic.gdx.graphics.Color.rgba8888(Color.red(color),Color.green(color),Color.blue(color),Color.alpha(color));

                        Log.d("Int Color", String.valueOf(color));
                        Log.d("Red", Integer.toString(Color.red(color)));
                        Log.d("Green", Integer.toString(Color.green(color)));
                        Log.d("Blue", Integer.toString(Color.blue(color)));

                        Log.d("Pure Hex", Integer.toHexString(color));
                        Log.d("#Hex no alpha", String.format("#%06X", (0xFFFFFF & color)));
                        Log.d("#Hex with alpha", String.format("#%08X", (0xFFFFFFFF & color)));
                        String hex_color=String.format("#%06X", (0xFFFFFF & color));

                        hex_color=hex_color.concat("FF");

                        Log.d("String hex:",hex_color );
                        pref.set_color(hex_color);

                        cp.hide();
                    }
                });
            }
        });
        imgButton=(Button)findViewById(R.id.img_button);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Matisse.from(MainActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(2);
                  */
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent,PICK_IMAGE);

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x=Integer.parseInt(t.getText().toString());
                pref.set_size(x);
                pref.set_velocity(seekBar.getProgress()*6);

                Intent intent = new Intent();
                intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                startActivity(intent);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK) {
            //TODO: action
            Log.d("Intent data: " ,data.toString());
            String s=getRealPathFromURI(data.getData());
            s=s.replace(Environment.getExternalStorageDirectory().toString(),"");
            pref.set_path(s);
            Log.d("Intent s: " ,s);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



}
