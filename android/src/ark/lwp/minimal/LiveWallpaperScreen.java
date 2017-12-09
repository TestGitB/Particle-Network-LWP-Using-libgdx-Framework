package ark.lwp.minimal;

import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import javax.microedition.khronos.opengles.GL10;

import static com.badlogic.gdx.Gdx.gl;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class LiveWallpaperScreen  implements Screen {
    Game game;
    FPSLogger logger;

    OrthographicCamera camera;
    Texture textureBg;
    SpriteBatch batcher;
    World world;
    ShapeRenderer sr,sr2;
    int particle_count;
    float dist;
    float velocity;
    int radius;
    int touch;
    int thickness;
    int line_length;
    Vector2 pos2;

    Color color;
    Color lcolor;
    Sprite sprite;
    Particle particle[];

    //Resolution of device
    int max_height=Gdx.graphics.getHeight();
    int max_width=Gdx.graphics.getWidth();

    public LiveWallpaperScreen(final Game game) {
        this.game = game;

        particle_count=SettingsPref.size;
        velocity=SettingsPref.velocity;
        radius= SettingsPref.radius;
        touch=SettingsPref.touch;
        thickness=SettingsPref.thickness;
        line_length=SettingsPref.line_length;
        camera = new OrthographicCamera(max_width, max_height);
        camera.position.set(camera.viewportWidth/2 , camera.viewportHeight/2 , 0);
        batcher = new SpriteBatch();


        //////////////////////////new particle system//////////////////////
        particle=new Particle[200];
        for (int i=0;i<200;i++)
        {
            particle[i]=new Particle(max_width,max_height,velocity);
        }




        if(SettingsPref.path.equals("0")&&SettingsPref.back_color.equals("0"))
            textureBg = new Texture("b.jpg");

        else if(SettingsPref.path.equals("0") && !SettingsPref.back_color.equals("0")) {

            textureBg = new Texture("a.jpg");
            batcher.setColor(Color.valueOf(SettingsPref.back_color));

        }
        else {
            batcher=new SpriteBatch();
            if(Gdx.files.external(SettingsPref.path).exists()) {
                textureBg = new Texture(Gdx.files.external(SettingsPref.path));

            }
            else
                textureBg = new Texture("b.jpg");
        }

        sprite=new Sprite(textureBg);
        float scale_factor= max(max_height/sprite.getHeight(),max_width/sprite.getWidth());
        sprite.setSize(sprite.getWidth()*scale_factor,sprite.getHeight()*scale_factor);
        sprite.scale(scale_factor);
        Log.d("Sprite size:", String.valueOf(sprite.getHeight()+ " "+String.valueOf(sprite.getWidth())));
        logger = new FPSLogger();


        world = new World(new Vector2(0, 0), true);

        sr = new ShapeRenderer();
        sr2=new ShapeRenderer();

        color = new Color(Color.valueOf(SettingsPref.color));
        lcolor=new Color(Color.valueOf(SettingsPref.line_color));

        Log.d("LWP Created","True");
    }

    private void draw(float delta)
    {
        world.step(1f/360f, 6, 2);
        camera.update();
        gl.glClearColor(color.r, color.g, color.b, color.a);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.begin();
        batcher.draw(sprite,(max_width-sprite.getWidth())/2,(max_height-sprite.getHeight())/2,sprite.getWidth(),sprite.getHeight());
        batcher.end();

        //logger.log();
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(batcher.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr2.begin(ShapeRenderer.ShapeType.Filled);
        sr2.setColor(color);
        sr.setColor(color);

        for (int i=0;i<particle_count;i++)
       {
           particle[i].update();
           sr2.circle(particle[i].x,particle[i].y,radius);
           for(int j=0;j<particle_count;j++)
           if(Particle.distance(particle[i],particle[j])<line_length &&i!=j)
           {
                dist=Particle.distance(particle[i],particle[j]);
                sr.rectLine(particle[i].x, particle[i].y, particle[j].x, particle[j].y,thickness, new Color(lcolor.r,lcolor.g,lcolor.b,1-(dist/(line_length-70))),new Color(lcolor.r,lcolor.g,lcolor.b,1-(dist/(line_length-70))));
           }

            particle[i].check_bounds();

           if(touch==1&&Gdx.input.isTouched())
           {
               //Log.d("is touched",String.valueOf(Gdx.input.getX())+" "+String.valueOf(Gdx.input.getY()));
               pos2=new Vector2(Gdx.input.getX(),abs(Gdx.input.getY()-max_height));

                   if (Particle.get_pos(particle[i]).dst(pos2) < line_length+200)
                   {
                       dist = Particle.get_pos(particle[i]).dst(pos2);
                       sr.rectLine(particle[i].x,particle[i].y, pos2.x, pos2.y,thickness, new Color(lcolor.r,lcolor.g,lcolor.b, 1 - (dist / (line_length+130))), new Color(lcolor.r,lcolor.g,lcolor.b, 1 - (dist / (line_length+130))));
                   }


           }

       }

        sr.end();
        sr2.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void dispose() {

        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

        Log.d("LWP hidden","True");
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

        Log.d("LWP paused","True");
    }

    @Override
    public void render(float delta) {
        draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        Log.d("LWP resumed","True");
        particle_count=SettingsPref.size;
        velocity=SettingsPref.velocity;
        radius= SettingsPref.radius;
        touch=SettingsPref.touch;
        line_length=SettingsPref.line_length;
        thickness=SettingsPref.thickness;

        for(int i=0;i<particle_count;i++)
            particle[i].velocity=velocity/300;

        if(SettingsPref.path.equals("0")&&SettingsPref.back_color.equals("0"))
            textureBg = new Texture("b.jpg");

        else if(SettingsPref.path.equals("0") && !SettingsPref.back_color.equals("0")) {

            textureBg = new Texture("a.jpg");
            batcher.setColor(Color.valueOf(SettingsPref.back_color));

        }
        else {
            batcher=new SpriteBatch();
            if(Gdx.files.external(SettingsPref.path).exists()) {
                textureBg = new Texture(Gdx.files.external(SettingsPref.path));

            }
            else
                textureBg = new Texture("b.jpg");
        }

        sprite=new Sprite(textureBg);
        float scale_factor= max(max_height/sprite.getHeight(),max_width/sprite.getWidth());
        sprite.setSize(sprite.getWidth()*scale_factor,sprite.getHeight()*scale_factor);
        sprite.scale(scale_factor);

        color = new Color(Color.valueOf(SettingsPref.color));
        lcolor=new Color(Color.valueOf(SettingsPref.line_color));


    }


    @Override
    public void show() {
        // TODO Auto-generated method stub

        Log.d("LWP show","True");
    }
}
