package com.projectoop.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

public class Lava extends InteractiveTileObject{
    public Lava(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.LAVA_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Lava", "Collision");
    }

    @Override
    public void onFootHit() {
        Gdx.app.log("Lava", "Collision");
        AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class).play();
    }
}
