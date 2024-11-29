package com.projectoop.game.sprites.effectedObject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;

public abstract class EffectedObject extends Sprite {
    public enum State {NOTUSE, USING, USED};
    public State currentState;
    public State previousState;

    protected float stateTime;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean used;
    protected boolean using;

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public EffectedObject(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        currentState = State.NOTUSE;
        setPosition(x, y + 8/ GameWorld.PPM);
        defineObject();
        velocity = new Vector2(0, 0);
        b2body.setActive(false);
    }

    protected abstract void defineObject();
    protected abstract void prepareAnimation();
    protected abstract void prepareAudio();
    public abstract void update(float dt);
    public abstract void usingCallBack();
    public abstract void hitOnHead();

}
