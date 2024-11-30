package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class BossBall extends Bullet{
    private final float scaleX = 1.2f;
    private final float scaleY = 1.2f;

    public final int damage = 10;

    TextureAtlas atlasShooting;
    private Animation<TextureRegion> shootingAnim;
    public BossBall(PlayScreen screen, float x, float y, int direction) {
        super(screen, x, y, direction);
        this.velocity = new Vector2(2*direction , -0.2f);
    }
    @Override
    protected void prepareAnimation() {
        int rnd1 = MathUtils.random(2);
        switch (rnd1) {
            case 0:
                atlasShooting = new TextureAtlas("E_FlyingEye/Pack/FireBall.pack");
                break;
            default:
                atlasShooting = new TextureAtlas("Demon/Demon.pack");
                break;
        }
        // atlasShooting = new TextureAtlas("E_FlyingEye/Pack/FireBall.pack");
        shootingAnim = new Animation<TextureRegion>(0.1f, atlasShooting.getRegions());
    }
    @Override
    protected void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);
        CircleShape shape = new CircleShape();
        shape.setRadius(15/ GameWorld.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = GameWorld.BOSSBALL_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | GameWorld.KNIGHT_BIT |
            GameWorld.CHEST_BIT | GameWorld.ITEM_BIT | GameWorld.TRAP_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
    @Override
    public void destroy() {
        setToDestroy = true;
    }
    public TextureRegion getFrame(float dt){
        stateTime += dt;
        return shootingAnim.getKeyFrame(stateTime, true);
    }
    @Override
    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if (!destroyed){
            TextureRegion frame = getFrame(dt);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2+10/GameWorld.PPM);
            setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM * scaleX,
                frame.getRegionHeight() / GameWorld.PPM * scaleY);
            if ((!isShootingRight) && !frame.isFlipX()){
                frame.flip(true, false);
                isShootingRight = false;
            }
            else if ((isShootingRight) && frame.isFlipX()){
                frame.flip(true, false);
                isShootingRight = true;
            }
            b2body.setLinearVelocity(velocity);
            setRegion(frame);  // Set texture region for rendering
        }
    }
    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }
    @Override
    public void dispose() {
        if (atlasShooting != null) {
            atlasShooting.dispose();
        }
    }
}
