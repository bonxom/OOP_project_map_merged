package com.projectoop.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.Hud;
import com.projectoop.game.sprites.enemy.Goomba;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.tools.B2WorldCreator;
import com.projectoop.game.tools.WorldContactListener;

public class PlayScreen implements Screen {
    private GameWorld game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;//resize screen
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Knight player;
    private Goomba goomba;

    private Music music;

    public PlayScreen(GameWorld gameWorld){
        atlas = new TextureAtlas("KnightAsset/Mario_and_Enemies.pack");

        this.game = gameWorld;

        gameCam = new OrthographicCamera();
        //gamePort = new StretchViewport(GameWorld.V_WIDTH, GameWorld.V_HEIGHT, gameCam); // freely broaden in any direction, change the PNG ratio
        //gamePort = new ScreenViewport(gameCam); //broaden scale, unchange PNG ratio
        gamePort = new FitViewport(GameWorld.V_WIDTH / GameWorld.PPM, GameWorld.V_HEIGHT / GameWorld.PPM, gameCam);// broaden scale in any direction, unchange PNG ratio
        hud = new Hud(gameWorld.batch);


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/GameWorld.PPM);
        gameCam.position.set( gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0, -10), true);//vector gravity
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);
        player = new Knight(this);
        goomba = new Goomba(this, .32f, .32f);

        world.setContactListener(new WorldContactListener());

        music = AudioManager.manager.get(AudioManager.backgroundMusic, Music.class);
        music.setVolume(music.getVolume() - 0.7f);
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt){//dt = data time
        handleInput(dt);
        world.step(1/60f, 6, 2);

        player.update(dt);
        goomba.update(dt);

        hud.update(dt);
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//2 instructions for clear screen;

        renderer.render();//map

        b2dr.render(world, gameCam.combined);//box2d

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);//select camera position
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
