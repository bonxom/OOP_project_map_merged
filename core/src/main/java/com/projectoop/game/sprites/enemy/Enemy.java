package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected TextureAtlas atlasWalking;
    protected Animation<TextureRegion> walkAnimation;

    protected float stateTime;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean runningRight;

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        runningRight = true;
    }

    protected abstract void defineEnemy();
    protected abstract void prepareAnimation();
    public abstract void update(float dt);
    public abstract void hitOnHead();

    public void reverseVelocity(boolean x, boolean y){
        if (x){
            velocity.x = -velocity.x;
        }
        if (y){
            velocity.y = -velocity.y;
        }
    }
}
