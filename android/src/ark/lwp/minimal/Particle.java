package ark.lwp.minimal;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.cosDeg;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.sinDeg;

/**
 * Created by JAYAN on 08-12-2017.
 */

public class Particle {
    float x;
    float y;
    static float velocity;
    float angle;
    static int max_width;
    static int max_height;
    static int bound_width_left;
    static int bound_width_right;
    static int bound_height_top;
    static int bound_height_bottom;


    public Particle(int max_width,int max_height,float vel)
    {
        x=random(max_width);
        y=random(max_height);
        velocity=vel/300;
        angle=random(0,360);
        this.max_height=max_height;
        this.max_width=max_width;
        /*bound_height_top=-(max_width)/4;
        bound_width_left=-(max_width)/4;
        bound_height_bottom=max_height +max_width/4;
        bound_width_right=max_width+max_width/4;
        */

    }
    public void update()
    {
        x+=(velocity*cosDeg(angle));
        y+=(velocity*sinDeg(angle));
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public void check_bounds()
    {
        if(x>max_width+50)
        {
            angle=random(90,270);
        }
        else if(x<-50)
        {
            angle=random(-90,90);
        }

        if(y>max_height+50)
        {
            angle=random(-180,0);
        }
        else if(y<-50)
        {
            angle=random(0,180);
        }
    }

    public static  int distance(Particle p1,Particle p2)
    {
        return (int)Vector2.dst(p1.x,p1.y,p2.x,p2.y);
    }
    public static Vector2 get_pos(Particle p)
    {
        return new Vector2(p.x,p.y);
    }
}
