package com.projectoop.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.PlayerEnergyBar;
import com.projectoop.game.scences.PlayerHealthBar;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.tools.B2WorldCreator;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class PlayScreen implements Screen {
    protected PlayerHealthBar healthBar; // test
    protected PlayerEnergyBar energyBar;
    public GameWorld game;

    protected OrthographicCamera gameCam;
    protected Viewport gamePort;//resize screen
    //private Hud hud;

    protected TmxMapLoader mapLoader;
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;

    protected World world;
    protected Box2DDebugRenderer b2dr;
    public B2WorldCreator creator;
    // test
    protected Knight player;

    protected Array<Item> items;
    public Array<Item> itemsToRemove;
    protected LinkedBlockingQueue<ItemDef> itemsToSpawn;

    protected Music music;
    public boolean passThisRound;
    public int kill;

    public PlayScreen(GameWorld gameWorld){
        this.game = gameWorld;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(GameWorld.V_WIDTH / GameWorld.PPM, GameWorld.V_HEIGHT / GameWorld.PPM, gameCam);// broaden scale in any direction, unchange PNG ratio

        passThisRound = false;
        kill = 0;
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

    public abstract void spawnItem(ItemDef idef);
    public abstract void update(float dt);
    public abstract void show();
    public abstract void render(float delta);
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
        //hud.dispose();
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
