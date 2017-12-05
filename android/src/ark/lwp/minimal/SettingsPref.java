package ark.lwp.minimal;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JAYAN on 01-12-2017.
 */

public class SettingsPref {
     static int size=30;
     static int velocity=300;

     static String path="0";
     static String color="#FFFFFFFF";
     SharedPreferences pref;
     SharedPreferences.Editor editor;
     SettingsPref(Context context) {
          pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
          editor = pref.edit();

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
     void set_velocity(int x)
     {
          editor.putInt("velocity",x);
          editor.commit();
          velocity=x;
     }
     int get_velocity()
     {
          velocity=pref.getInt("velocity",300);
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
          color=pref.getString("color","#FFFFFFFF");
          return color;
     }
}
