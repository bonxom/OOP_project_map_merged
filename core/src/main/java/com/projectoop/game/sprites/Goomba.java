package com.projectoop.game.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Goomba extends Enemy{
    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ GameWorld.PPM, 100/GameWorld.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | //this Goomba also collide with others enemies or objects
                GameWorld.SPIKE_BIT | GameWorld.LAVA_BIT | GameWorld.ENEMY_BIT | GameWorld.OBJECT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
