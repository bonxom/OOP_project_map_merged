package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;

public class Portal extends InteractiveTileObject{
    public Portal(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.PORTAL_BIT);
    }

    @Override
    public void onFootHit(Knight knight) {
    }

    public void passThisRound(Knight player){
        System.out.println("max_monster: " + screen.creator.getGroundEnemies().size + "  killed: " + screen.kill);
        if (screen.creator.getGroundEnemies() == null || screen.creator.getGroundEnemies().size <= screen.kill){
            screen.passThisRound = true;
        }

    }
}
