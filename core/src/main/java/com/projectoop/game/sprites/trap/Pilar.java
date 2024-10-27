package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.math.Rectangle;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Pilar extends InteractiveTileObject {
    public Pilar(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.OBJECT_BIT);
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onFootHit() {

    }
}
