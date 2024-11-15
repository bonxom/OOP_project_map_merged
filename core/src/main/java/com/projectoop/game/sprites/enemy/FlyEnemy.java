package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class FlyEnemy extends GroundEnemy{//test th, code sau
    public FlyEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y + 30/GameWorld.PPM, 0, 1);
    }

    @Override
    protected void defineEnemy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.ENEMY_BIT | GameWorld.CHEST_BIT |
            GameWorld.PILAR_BIT | GameWorld.KNIGHT_BIT | GameWorld.ARROW_BIT;
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

    public State getState(){
        //die and hurt code
        if (isDie){//test
            if (dieAnimation.isAnimationFinished(stateTime)) {
                destroy();
            }
            //call destroy
            return State.DEAD;
        }

        if (isHurt){
            isHurt = false;
            isHurting = true;
            lastDirectionIsRight = runningRight;
            this.velocity = new Vector2(0, 0);
            return State.HURTING;
        }
        if(isHurting) {//test
            if(!hurtAnimation.isAnimationFinished(stateTime)) {
                System.out.println("hihihihihihihh");
                return State.HURTING;
            }
            else {
                isHurting = false;
                this.velocity = lastDirectionIsRight ? new Vector2(1, 0) : new Vector2(-1, 0);
            }
        }
        //attack code
        if (isAttack){
            isAttacking = true;
            isAttack = false;
            lastDirectionIsRight = runningRight;
            this.velocity = new Vector2(0, 0);
            return State.ATTACKING;
        }
        if (isAttacking){//test
            if (!attackAnimation.isAnimationFinished(stateTime)){
                System.out.println("attacking");
                return State.ATTACKING;
            }
            else {
//                int dir = (runningRight) ? 1 : -1;
//                PlayScreen.bulletManager.addBullet(b2body.getPosition().x, b2body.getPosition().y, dir, "FireBall");
                isAttacking = false;
                this.velocity = lastDirectionIsRight ? new Vector2(1, 0) : new Vector2(-1, 0);
                //playSound1 = false;
            }
        }

        return State.WALKING;
    }
}
