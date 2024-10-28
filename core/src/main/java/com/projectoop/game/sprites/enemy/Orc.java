package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Orc extends Enemy{

    public Orc(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        prepareAnimation();
    }

    protected void prepareAnimation(){
        atlasWalking = new TextureAtlas("OrcAsset/Pack/Walk.pack");

        walkAnimation = new Animation<TextureRegion>(0.1f, atlasWalking.getRegions());
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(60/ GameWorld.PPM, 60/GameWorld.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | //collide list
            GameWorld.SPIKE_BIT | GameWorld.LAVA_BIT | GameWorld.ENEMY_BIT |
            GameWorld.OBJECT_BIT | GameWorld.KNIGHT_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if (!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            TextureRegion frame = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);

            if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !frame.isFlipX()){
                frame.flip(true, false);
                runningRight = false;
            }
            else if ((b2body.getLinearVelocity().x > 0 || runningRight) && frame.isFlipX()){
                frame.flip(true, false);
                runningRight = true;
            }

            setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM * 1.5f, frame.getRegionHeight() / GameWorld.PPM * 1.5f);
            setRegion(frame);
        }
    }

    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {

    }
}
