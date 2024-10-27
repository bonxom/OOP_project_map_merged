package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;
import jdk.jshell.Snippet;

public class Lava extends InteractiveTileObject {
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
