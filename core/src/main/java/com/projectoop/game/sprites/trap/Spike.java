package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.badlogic.gdx.math.Rectangle;
import com.projectoop.game.scences.Hud;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

public class Spike extends InteractiveTileObject {
    public Spike (PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.SPIKE_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Spike", "Collision");
        setCategoryFilter(GameWorld.DESTROYED_BIT);
        //getCell().setTile(null);
        Hud.addScore(200);
        AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class).play();
    }

    @Override
    public void onFootHit() {
        Gdx.app.log("Spike", "Collision");
        AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class).play();
        screen.getPlayer().setDie();
    }

}
