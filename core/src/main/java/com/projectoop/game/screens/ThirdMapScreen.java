package com.projectoop.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.PlayerEnergyBar;
import com.projectoop.game.scences.PlayerHealthBar;
import com.projectoop.game.sprites.effectedObject.EffectedObject;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.enemy.GroundEnemy;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.sprites.items.Potion;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.tools.B2WorldCreator;
import com.projectoop.game.tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class ThirdMapScreen extends PlayScreen {

    public ThirdMapScreen(GameWorld gameWorld){
        super(gameWorld);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/GameWorld.PPM);
        gameCam.position.set( gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0, -10), true);//vector gravity
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        player = new Knight(this);
        healthBar = new PlayerHealthBar(this); // test
        energyBar = new PlayerEnergyBar(this);
        world.setContactListener(new WorldContactListener(this));

        music = AudioManager.manager.get(AudioManager.backgroundMusic, Music.class);
        music.setVolume(music.getVolume());
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


    public void update(float dt){//dt = delta time
        handleInput(dt);
        handleSpawningItems();

        //FPS = 60
        world.step(1/60f, 6, 2);

        player.update(dt);

        for (Enemy enemy : creator.getGroundEnemies()){
            if (enemy.inRangeAttack && player.isAttack() && enemy.getCurrentState() != Enemy.State.HURTING) {
                enemy.hurtingCallBack();
            }
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

        //hud.update(dt);

        //attack gamecam to x coordinate of player
        if (player.b2body.getPosition().x >= gamePort.getWorldWidth() /2 &&
            player.b2body.getPosition().x <= 160 * 16 / GameWorld.PPM - gamePort.getWorldWidth() / 2) {
            gameCam.position.x = player.b2body.getPosition().x;
        }
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
        healthBar.draw(delta);
        energyBar.draw(delta);
        game.batch.end();

        //draw head of display
        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);//select camera position
        //hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if (passThisRound){
            game.setScreen(new FourthMapScreen(game));
            dispose();
        }
    }


}
