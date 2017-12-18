package ark.lwp.minimalfree;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.min;

/**
 * Created by JAYAN on 01-12-2017.
 */

public class SettingsPref {
     static int size=30;
     static float velocity=300;
     static String path="0";
     static String color="#000000FF";
     static String line_color="#000000fF";
     static int radius=5;
     static String back_color="#46A7F5FF";
     static int touch=1;
     static int line_length =300;
     static int thickness=2;
     SharedPreferences pref;
     SharedPreferences.Editor editor;
     SettingsPref(Context context) {
          pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
          editor = pref.edit();

          if(pref.getBoolean("firstrun",true))
          {
               Log.d("'First run ","true");
               line_length=min(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
               line_length=line_length/2 -50;
               Log.d("'linelenght ",String.valueOf(line_length));

               editor.putBoolean("firstrun",false);
               editor.putInt("line_length",line_length);
               editor.commit();
          }
     }
     void set_size(int x)
     {

          editor.putInt("size",x);
          editor.commit();
          size=x;
     }
     int get_size()
     {
          size=pref.getInt("size",30);
          return size;
     }
     void set_velocity(float x)
     {
          editor.putFloat("velocity",x);
          editor.commit();
          velocity=x;
     }
     float get_velocity()
     {
          velocity=pref.getFloat("velocity",300);
          return velocity;
     }

     void set_path(String x)
     {
          editor.putString("path",x);
          editor.commit();
          path=x;
     }
     String get_path()
     {
          path=pref.getString("path","0");
          return path;
     }

     void set_color(String x)
     {
          editor.putString("color",x);
          editor.commit();
          color=x;
     }
     String get_color()
     {
          color=pref.getString("color","#000000FF");
          return color;
     }

     void set_back_color(String x)
     {
          editor.putString("back_color",x);
          editor.commit();
          back_color=x;
     }
     String get_back_color()
     {
          back_color=pref.getString("back_color","#46A7F5FF");
          return back_color;
     }

     void set_radius(int x)
     {

          editor.putInt("radius",x);
          editor.commit();
          radius=x;
     }
     int get_radius()
     {
          radius=pref.getInt("radius",5);
          return radius;
     }


     void set_line_color(String x)
     {
          editor.putString("line_color",x);
          editor.commit();
          line_color=x;
     }
     String get_line_color()
     {
          line_color=pref.getString("line_color","#000000FF");
          return line_color;
     }

     void set_touch(int x)
     {

          editor.putInt("touch",x);
          editor.commit();
          touch=x;
     }
     int get_touch()
     {
          touch=pref.getInt("touch",1);
          return touch;
     }


     void set_line_length(int x)
     {

          editor.putInt("line_length",x);
          editor.commit();
          line_length=x;
     }
     int get_line_length()
     {
          line_length=pref.getInt("line_length",300);
          Log.d("'linel",String.valueOf(line_length));

          return line_length;
     }

     void set_thickness(int x)
     {

          editor.putInt("thick",x);
          editor.commit();
          thickness=x;
     }
     int get_thickness()
     {
          thickness=pref.getInt("thick",2);
          return thickness;
     }
}
