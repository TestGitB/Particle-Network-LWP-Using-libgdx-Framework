package ark.lwp.minimal;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JAYAN on 01-12-2017.
 */

public class SettingsPref {
     static int size=30;
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

}
