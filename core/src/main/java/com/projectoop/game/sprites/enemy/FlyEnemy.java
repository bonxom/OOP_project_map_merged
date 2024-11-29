package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.weapons.BulletManager;

public class FlyEnemy extends GroundEnemy{//test th, code sau
    private final float COOL_DOWN = 2;
    private float timeCount;
    private float lastTimeShoot;
    private BulletManager bulletManager;
    public FlyEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y, 0, 1, 10);
        lastTimeShoot = 0;
        timeCount = 2;
        bulletManager = new BulletManager(screen);
    }

    @Override
    protected void defineEnemy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(9/GameWorld.PPM, 9/GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT |
            GameWorld.PILAR_BIT | GameWorld.ARROW_BIT |
            GameWorld.KNIGHT_SWORD_LEFT | GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void prepareAnimation() {
        atlasWalking = new TextureAtlas("E_FlyingEye/Pack/Flight.pack");
        atlasAttacking = new TextureAtlas("E_FlyingEye/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("E_FlyingEye/Pack/Death.pack");
        atlasHurting = new TextureAtlas("E_FlyingEye/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.3f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }

    @Override
    public TextureRegion getFrame(float dt) {
        timeCount += dt;
        //System.out.println("TimeCount: " + timeCount);
        if(timeCount > COOL_DOWN) {
            System.out.println("khac dom");
            int direction = (runningRight) ? 1 : -1;
            bulletManager.addBullet(b2body.getPosition().x, b2body.getPosition().y, direction, "FireBall");
            timeCount = 0;
        }
        return super.getFrame(dt);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        bulletManager.update(dt);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        bulletManager.draw(batch);
    }

    @Override
    public void dispose() {

    }
}
