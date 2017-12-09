package ark.lwp.minimal;

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
    float velocity;
    float angle;
    int max_width;
    int max_height;

    public Particle(int max_width,int max_height,float vel)
    {
        x=random(max_width);
        y=random(max_height);
        velocity=vel/300;
        angle=random(0,360);
        this.max_height=max_height;
        this.max_width=max_width;
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
        if(x>max_width+200)
        {
            x=random(-200,-100);
            y=random(max_height);
            angle=random(0,360);
        }
        else if(x<-200)
        {
            x=random(max_width+100,max_width+200);
            y=random(max_height);
            angle=random(0,360);
        }




        if(y>max_height+200)
        {
            y=random(-200,-100);
            x=random(max_width);
            angle=random(0,360);
        }
        else if(y<-200)
        {
            y=random(max_height+100,max_height+200);
            x=random(max_width);
            angle=random(0,360);
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
