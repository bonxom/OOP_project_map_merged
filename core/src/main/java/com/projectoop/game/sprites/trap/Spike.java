package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import com.projectoop.game.GameWorld;
import com.badlogic.gdx.math.Rectangle;
import com.projectoop.game.scences.Hud;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.tools.AudioManager;


public class Spike extends InteractiveTileObject {
    public Spike (PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.SPIKE_BIT);
    }

    @Override
    public void onFootHit(Knight knight) {
        Gdx.app.log("Spike", "Collision");
        //AudioManager.manager.get(AudioManager.knightDieAudio, Sound.class).play();
        screen.getPlayer().setDie();
    }
}
