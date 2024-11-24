package com.projectoop.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.Hud;
import com.projectoop.game.scences.PlayerHealthBar;
import com.projectoop.game.sprites.effectedObject.EffectedObject;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.enemy.GroundEnemy;
import com.projectoop.game.sprites.enemy.Orc;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.sprites.items.Potion;
import com.projectoop.game.sprites.weapons.Arrow;
import com.projectoop.game.sprites.weapons.BulletManager;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.tools.B2WorldCreator;
import com.projectoop.game.tools.WorldContactListener;

import java.awt.desktop.SystemSleepEvent;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
    public GameWorld game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;//resize screen
    private Hud hud;
    protected PlayerHealthBar healthbar; // test

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    public B2WorldCreator creator;

    private Knight player;

    private Array<Item> items;
    public Array<Item> itemsToRemove;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private Music music;

    public PlayScreen(GameWorld gameWorld){

        this.game = gameWorld;

        gameCam = new OrthographicCamera();
        //gamePort = new StretchViewport(GameWorld.V_WIDTH, GameWorld.V_HEIGHT, gameCam); // freely broaden in any direction, change the PNG ratio
        //gamePort = new ScreenViewport(gameCam); //broaden scale, unchange PNG ratio
        gamePort = new FitViewport(GameWorld.V_WIDTH / GameWorld.PPM, GameWorld.V_HEIGHT / GameWorld.PPM, gameCam);// broaden scale in any direction, unchange PNG ratio
        hud = new Hud(gameWorld.batch);
        healthbar = new PlayerHealthBar(this);


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/GameWorld.PPM);
        gameCam.position.set( gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0, -10), true);//vector gravity
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        player = new Knight(this);
        world.setContactListener(new WorldContactListener(this));

        music = AudioManager.manager.get(AudioManager.backgroundMusic, Music.class);
        music.setVolume(music.getVolume() - 0.7f);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToRemove = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    //add item that need to be spawned to a Queue
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    //spawn all item in Queue
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Potion.class){
                items.add(new Potion(this, idef.position.x, idef.position.y));
            }
        }
    }

    //Input manager
    public void handleInput(float dt){
        if (player.getState() != Knight.State.DEAD && player.getState() != Knight.State.HURTING) {
            //test
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) && player.b2body.getLinearVelocity().y == 0) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }
            if (player.getState() != Knight.State.ATTACKING3) {
                if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                //test
                if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
                    player.bigMode();
                }
            }

            //attacking code
            if (Gdx.input.isKeyPressed(Input.Keys.J)){
                player.attack1CallBack();
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.K)){
                player.attack2CallBack();
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
                player.attack3CallBack();
            }
        }
    }

    public void update(float dt){//dt = data time
        handleInput(dt);
        handleSpawningItems();

        //FPS = 60
        world.step(1/60f, 6, 2);

        player.update(dt);

        for (Enemy enemy : creator.getGroundEnemies()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() + (GameWorld.V_WIDTH/2 + 4 * 16)/GameWorld.PPM){
                enemy.b2body.setActive(true);//optimize to avoid lagging
            }
        }

        for (EffectedObject eobj : creator.getChests()){
            eobj.update(dt);
            if (eobj.getX() < player.getX() + (GameWorld.V_WIDTH/2 + 4 * 16)/GameWorld.PPM){
                eobj.b2body.setActive(true);
            }
        }

        for (Item item : items){
            item.update(dt);
        }
        items.removeAll(itemsToRemove, true);

        hud.update(dt);

        //attack gamecam to x coordinate of player
        gameCam.position.x = player.b2body.getPosition().x;
        //gameCam.position.y = player.b2body.getPosition().y + GameWorld.V_HEIGHT/4/GameWorld.PPM;

        //update with correct coordinate
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

        //draw all sprite
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getGroundEnemies()){
            enemy.draw(game.batch);
        }

        for (EffectedObject eobj : creator.getChests()){
            eobj.draw(game.batch);
        }
        for (Item item : items){
            item.draw(game.batch);
        }
        healthbar.draw(delta);
        game.batch.end();

        //draw head of display
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);//select camera position
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if (player.isEndGame() && player.getStateTimer() > 0.5) return true;
        return false;
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

    public Knight getPlayer() {
        return player;
    }

    public SpriteBatch getBatch() {
        return game.batch;
    } // test

    public Viewport getGamePort() {
        return gamePort;
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }
}
