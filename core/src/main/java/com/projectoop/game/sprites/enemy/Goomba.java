package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Goomba extends Enemy {

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/GameWorld.PPM, 16/GameWorld.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));//smashed texture
            stateTime = 0;
        }
        else if (!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }
    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(60/ GameWorld.PPM, 70/GameWorld.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | //collide list
                GameWorld.SPIKE_BIT | GameWorld.LAVA_BIT | GameWorld.ENEMY_BIT |
                GameWorld.PILAR_BIT | GameWorld.KNIGHT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //goomba's head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1/GameWorld.PPM);
        vertice[1] = new Vector2(5, 8).scl(1/GameWorld.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1/GameWorld.PPM);
        vertice[3] = new Vector2(3, 3).scl(1/GameWorld.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;//the Character is bounded up
        fdef.filter.categoryBits = GameWorld.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void prepareAnimation() {

    }

    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }
    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }
}
