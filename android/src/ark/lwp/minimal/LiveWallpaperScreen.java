package ark.lwp.minimal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.MODE_PRIVATE;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class LiveWallpaperScreen  implements Screen {
    Game game;
    FPSLogger logger;

    OrthographicCamera camera;
    Texture textureBg;
    Image img;
    SpriteBatch batcher;
    World world;
//    Box2DDebugRenderer debugRenderer;
    ShapeRenderer sr,sr2;
    Body body[];
    int particle_count;
    float dist;
    int velocity;
    Vector2 pos2;
    Body border;
    BodyDef bodyDef;
    FixtureDef fixtureDef;

    Color color;
    Sprite sprite;


    //Resolution of device
    int max_height=Gdx.graphics.getHeight();
    int max_width=Gdx.graphics.getWidth();

    public LiveWallpaperScreen(final Game game) {
        this.game = game;



        particle_count=SettingsPref.size;
        velocity=SettingsPref.velocity;
        body = new Body[particle_count];
        camera = new OrthographicCamera(max_width, max_height);
        camera.position.set(camera.viewportWidth/2 , camera.viewportHeight/2 , 0);


        if(SettingsPref.path=="0")
        textureBg = new Texture("b.jpg");
        else {
            textureBg = new Texture(Gdx.files.external(SettingsPref.path));
        }
        sprite=new Sprite(textureBg);
        float scale_factor= max(max_height/sprite.getHeight(),max_width/sprite.getWidth());
        sprite.setSize(sprite.getWidth()*scale_factor,sprite.getHeight()*scale_factor);
        sprite.scale(scale_factor);
        Log.d("Sprite size:", String.valueOf(sprite.getHeight()+ " "+String.valueOf(sprite.getWidth())));
       // img.setHeight();
       // img.setWidth();
        //img.setSize(img.getWidth()*scale_factor,img.getHeight()*scale_factor);
        //img.setSize(textureBg.getWidth()*scale_factor,textureBg.getHeight()*scale_factor);
        //img.setWidth(img.getImageWidth()*scale_factor);
        //img.setHeight(img.getImageHeight()*scale_factor);
        batcher = new SpriteBatch();
        logger = new FPSLogger();


        world = new World(new Vector2(0, 0), true);
        //debugRenderer = new Box2DDebugRenderer();
        //debugRenderer.setDrawBodies(true);
        //debugRenderer.setDrawVelocities(true);

        sr = new ShapeRenderer();
        sr2=new ShapeRenderer();
        bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape circle = new CircleShape();
        circle.setRadius(10f);

// Create a fixture definition to apply our shape to
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit

// Create our fixture and attach it to the body
        for (int i = 0; i < particle_count; i++) {
            circle.setRadius(4);
            body[i] = world.createBody(bodyDef);

            body[i].createFixture(fixtureDef);
            bodyDef.position.set(random(1, max_width-10), random(4, max_height-10));

            body[i].setLinearVelocity(random(-velocity,velocity), random(-velocity,velocity));

        }
        BodyDef bodyDef2=new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef2=new FixtureDef();
        bodyDef2.position.set(0, 0);
        ChainShape b=new ChainShape();
        b.createLoop(new Vector2[]{new Vector2(-50,-50),new Vector2(-50,max_height+50),new Vector2(max_width+50,max_height+50),new Vector2(max_width+50,-50)});
        border = world.createBody(bodyDef2);
        fixtureDef2.density=0f;
        fixtureDef2.friction=0f;
        fixtureDef2.restitution=0f;
        fixtureDef2.shape =b;
        border.createFixture(fixtureDef2);



// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

      //  SettingsPref.color=Color.rgba8888(android.graphics.Color.red(SettingsPref.color),android.graphics.Color.green(SettingsPref.color),android.graphics.Color.blue(SettingsPref.color),android.graphics.Color.alpha(SettingsPref.color));
        color = new Color(Color.valueOf(SettingsPref.color));
        Log.d("GdxColor",String.valueOf(color));
        Log.d("GdxColorBlack",String.valueOf(Color.BLACK));
    }

    private void draw(float delta)
    {
        //colorAction.act(1f/120f);

        world.step(1f/360f, 6, 2);
        camera.update();
        gl.glClearColor(color.r, color.g, color.b, color.a);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


//        logger.log();
        //debugRenderer.render(world,batcher.getProjectionMatrix());

        //debugRenderer.setDrawAABBs(true);

        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.begin();
        //batcher.draw(img,0,0,max_width,max_height);
        //sprite.draw(batcher);
        batcher.draw(sprite.getTexture(),(max_width-sprite.getWidth())/2,(max_height-sprite.getHeight())/2,sprite.getWidth(),sprite.getHeight());
        batcher.end();

        //logger.log();
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //sr.setColor(1,1,1,0.1f);
        sr.setProjectionMatrix(batcher.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr2.begin(ShapeRenderer.ShapeType.Filled);
        sr2.setColor(color);
        sr.setColor(color);

        for (int i=0;i<particle_count;i++)
       {
           sr2.circle(body[i].getPosition().x,body[i].getPosition().y ,body[i].getFixtureList().first().getShape().getRadius());

           for(int j=0;j<particle_count;j++)
           if(body[i].getPosition().dst(body[j].getPosition())<300 &&i!=j)
           {
               dist=body[i].getPosition().dst(body[j].getPosition());
               sr.line(body[i].getPosition().x, body[i].getPosition().y, body[j].getPosition().x, body[j].getPosition().y, new Color(color.r,color.g,color.b,1-(dist/250)),new Color(color.r,color.g,color.b,1-(dist/250)));
           }


           if(Gdx.input.isTouched())
           {
               //Log.d("is touched",String.valueOf(Gdx.input.getX())+" "+String.valueOf(Gdx.input.getY()));
               pos2=new Vector2(Gdx.input.getX(),abs(Gdx.input.getY()-max_height));

                   if (body[i].getPosition().dst(pos2) < 500)
                   {
                       dist = body[i].getPosition().dst(pos2);
                       sr.line(body[i].getPosition().x, body[i].getPosition().y, pos2.x, pos2.y, new Color(1, 1, 1, 1 - (dist / 400)), new Color(0, 0, 0, 1 - (dist / 400)));
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

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

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

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }
}
