package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.maps.MapObject;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.Knight;

public class Pilar extends InteractiveTileObject {
    public Pilar(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.PILAR_BIT);
    }

    @Override
    public void onFootHit(Knight knight) {
    }

    @Override
    public void passThisRound(Knight knight) {

    }
}
