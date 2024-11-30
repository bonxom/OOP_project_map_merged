package com.projectoop.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.enemy.GroundEnemy;
import com.projectoop.game.sprites.weapons.Arrow;
import com.projectoop.game.sprites.weapons.BulletManager;
import com.projectoop.game.tools.AudioManager;

public class Knight extends Sprite {
    public enum State {HURTING, ATTACKING1, ATTACKING2, ATTACKING3, DEAD, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    private PlayScreen screen;
    private World world;
    public Body b2body;
    private BulletManager bulletManager;

    public static float scaleX = 1.5f;
    public static float scaleY = 1.5f;

    private Vector2 startPosition;
    private int damage;

    //test
    public static int deathCount = 3;

    private TextureAtlas atlasRunning;
    private TextureAtlas atlasJumping;
    private TextureAtlas atlasStanding;
    private TextureAtlas atlasAttacking1;
    private TextureAtlas atlasAttacking2;
    private TextureAtlas atlasAttacking3;
    private TextureAtlas atlasBeingHurt;
    private TextureAtlas atlasDieing;

    private Animation<TextureRegion> knightRun;
    private Animation<TextureRegion> knightJump;
    private Animation<TextureRegion> knightAttack1;
    private Animation<TextureRegion> knightAttack2;
    private Animation<TextureRegion> knightAttack3;
    private Animation<TextureRegion> knightDie;
    private Animation<TextureRegion> knightHurt;
    private Animation<TextureRegion> knightStand;

    private Sound knightRunSound;
    private Sound knightJumpSound;
    private Sound knightSwordSound;
    private Sound knightArrowSound;
    private Sound knightHurtSound;
    private Sound knightDieSound;

    private float stateTimer;
    private float timeCountShoot;
    private float timeCountAttack;
    private float untilCount;
    private final float SHOOTING_COOL_DOWN = 1;
    private final float ATTACKING_COOL_DOWN = 0.5f;
    private float timeCountBig;
    private final float BIG_TIMER = 5;
    public final float UNTIL_COOL_DOWN = 10;

    private int health;
    private int healthMax;

    private boolean isRunningRight;
    private boolean isHurt;
    private boolean isHurting;
    private boolean isAttacking1;
    private boolean isAttacking2;
    private boolean isAttacking3;
    private boolean isAttack3;
    private boolean isDie;
    private boolean endGame;
    public boolean isBig;

    private boolean playSound1;
    private boolean playSound2;

    public Knight(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        bulletManager = new BulletManager(screen);
        currentState = State.STANDING;
        previousState = State.STANDING;

        startPosition = new Vector2(32/GameWorld.PPM, 100/GameWorld.PPM);

        stateTimer = 0;
        timeCountShoot = 2;
        timeCountAttack = 1;
        timeCountBig = 0;
        untilCount = UNTIL_COOL_DOWN;
        isRunningRight = true;

        health = 100;
        healthMax = 100;

        prepareAnimation();
        prepareSound();
        defineKnight();

        setBounds(0, 0, 16/GameWorld.PPM, 16/GameWorld.PPM);

        isAttacking1 = false; isAttacking2 = false; isAttacking3 = false;
        isAttack3 = false;
        isDie = false;
        isHurt = false;
        isHurting = false;
        endGame = false;
        isBig = false;
    }

    private void prepareAnimation(){
        atlasRunning = new TextureAtlas("KnightAsset/Pack/Run.pack");
        atlasJumping = new TextureAtlas("KnightAsset/Pack/Jump.pack");
        atlasAttacking1 = new TextureAtlas("KnightAsset/Pack/Attack01.pack");
        atlasAttacking2 = new TextureAtlas("KnightAsset/Pack/Attack02.pack");
        atlasAttacking3 = new TextureAtlas("KnightAsset/Pack/Attack03.pack");
        atlasDieing = new TextureAtlas("KnightAsset/Pack/Die.pack");
        atlasBeingHurt = new TextureAtlas("KnightAsset/Pack/Hurt.pack");
        atlasStanding = new TextureAtlas("KnightAsset/Pack/Idle.pack");

        knightRun = new Animation<TextureRegion>(0.05f, atlasRunning.getRegions());
        knightJump = new Animation<TextureRegion>(0.05f, atlasJumping.getRegions());
        knightStand = new Animation<TextureRegion>(0.1f, atlasStanding.getRegions());
        knightDie = new Animation<TextureRegion>(0.5f, atlasDieing.getRegions());
        knightAttack1 = new Animation<TextureRegion>(0.05f, atlasAttacking1.getRegions());
        knightAttack2 = new Animation<TextureRegion>(0.05f, atlasAttacking2.getRegions());
        knightAttack3 = new Animation<TextureRegion>(0.05f, atlasAttacking3.getRegions());
        knightHurt = new Animation<TextureRegion>(0.05f, atlasBeingHurt.getRegions());
    }

    private void prepareAnimation1(){//anim của nguyễn bá
        atlasRunning = new TextureAtlas("KnightAsset2/Pack/Run.pack");
        atlasJumping = new TextureAtlas("KnightAsset2/Pack/Jump.pack");
        atlasAttacking1 = new TextureAtlas("KnightAsset2/Pack/Attack01.pack");
        //atlasAttacking2 = new TextureAtlas("KnightAsset/Pack/Attack02.pack");
        //atlasAttacking3 = new TextureAtlas("KnightAsset/Pack/Attack03.pack");
        atlasDieing = new TextureAtlas("KnightAsset2/Pack/Die.pack");
        atlasBeingHurt = new TextureAtlas("KnightAsset2/Pack/Hurt.pack");
        atlasStanding = new TextureAtlas("KnightAsset2/Pack/Idle.pack");

        knightRun = new Animation<TextureRegion>(0.05f, atlasRunning.getRegions());
        knightJump = new Animation<TextureRegion>(0.05f, atlasJumping.getRegions());
        knightStand = new Animation<TextureRegion>(0.1f, atlasStanding.getRegions());
        knightDie = new Animation<TextureRegion>(0.5f, atlasDieing.getRegions());
        knightAttack1 = new Animation<TextureRegion>(0.05f, atlasAttacking1.getRegions());
        //knightAttack2 = new Animation<TextureRegion>(0.05f, atlasAttacking2.getRegions());
        //knightAttack3 = new Animation<TextureRegion>(0.05f, atlasAttacking3.getRegions());
        knightHurt = new Animation<TextureRegion>(0.01f, atlasBeingHurt.getRegions());
    }

    private void prepareSound(){
        knightJumpSound = AudioManager.manager.get(AudioManager.knightJumpAudio, Sound.class);
        knightRunSound = AudioManager.manager.get(AudioManager.knightRunAudio, Sound.class);
        knightHurtSound = AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class);
        knightDieSound = AudioManager.manager.get(AudioManager.knightDieAudio, Sound.class);
        knightSwordSound = AudioManager.manager.get(AudioManager.knightSwordAudio, Sound.class);
        knightArrowSound = AudioManager.manager.get(AudioManager.knightArrowAudio, Sound.class);

        playSound1 = false;
        playSound2 = false;
    }

    public void defineKnight(){
        damage = 20;

        BodyDef bdef = new BodyDef();
        bdef.position.set(startPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.KNIGHT_BIT;
        fdef.filter.maskBits =
            GameWorld.GROUND_BIT | GameWorld.FIREBALL_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT | GameWorld.CHEST1_BIT |
            GameWorld.ENEMY_BIT | GameWorld.ITEM_BIT | GameWorld.PORTAL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //make EdgeShape for checking foot-collision
        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-2/GameWorld.PPM, -6/GameWorld.PPM),
                new Vector2(2/GameWorld.PPM, -6/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_FOOT_BIT;
        fdef.shape = foot;
        //fdef.isSensor = true;//foot is a sensor, sensor is an object but is not able to make physical collision
        b2body.createFixture(fdef).setUserData(this);

        //sword hit right sensor
        EdgeShape swordRight = new EdgeShape();
        swordRight.set(new Vector2(20/GameWorld.PPM, -6/GameWorld.PPM),
            new Vector2(20/GameWorld.PPM, 10/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = swordRight;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //sword hit left sensor
        EdgeShape swordLeft = new EdgeShape();
        swordLeft.set(new Vector2(-20/GameWorld.PPM, -6/GameWorld.PPM),
            new Vector2(-20/GameWorld.PPM, 10/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_LEFT;
        fdef.shape = swordLeft;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void redefineKnight(){
        damage = 50;

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.KNIGHT_BIT;
        fdef.filter.maskBits =
            GameWorld.GROUND_BIT | GameWorld.FIREBALL_BIT | GameWorld.BOSSBALL_BIT |
                GameWorld.TRAP_BIT | GameWorld.CHEST_BIT | GameWorld.CHEST1_BIT |
                GameWorld.ENEMY_BIT | GameWorld.ITEM_BIT | GameWorld.PORTAL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //make EdgeShape for checking foot-collision
        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-20/GameWorld.PPM, -20/GameWorld.PPM),
            new Vector2(20/GameWorld.PPM, -20/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_FOOT_BIT;
        fdef.shape = foot;
        //fdef.isSensor = true;//foot is a sensor, sensor is an object but is not able to make physical collision
        b2body.createFixture(fdef).setUserData(this);

        //sword hit right sensor
        EdgeShape swordRight = new EdgeShape();
        swordRight.set(new Vector2(50/GameWorld.PPM, -20/GameWorld.PPM),
            new Vector2(50/GameWorld.PPM, 20/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = swordRight;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        swordRight.set(new Vector2(25/GameWorld.PPM, -20/GameWorld.PPM),
            new Vector2(25/GameWorld.PPM, 20/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = swordRight;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //sword hit left sensor
        EdgeShape swordLeft = new EdgeShape();
        swordLeft.set(new Vector2(-50/GameWorld.PPM, -20/GameWorld.PPM),
            new Vector2(-50/GameWorld.PPM, 20/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_LEFT;
        fdef.shape = swordLeft;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        swordLeft.set(new Vector2(-25/GameWorld.PPM, -20/GameWorld.PPM),
            new Vector2(-25/GameWorld.PPM, 20/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_SWORD_LEFT;
        fdef.shape = swordLeft;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void bigMode(){
        if (untilCount >= UNTIL_COOL_DOWN) {
            isBig = true;
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + 40 / GameWorld.PPM, 0);
            Knight.scaleX = Knight.scaleY = 3;
            Arrow.scaleX = Arrow.scaleY = 3;
            redefineKnight();
            GroundEnemy.attackRange = 70;
            untilCount = 0;
        }
    }

    public void endBigMode(){
        isBig = false;
        Knight.scaleX = Knight.scaleY = 1.5f;
        Arrow.scaleX = Arrow.scaleY = 1.2f;
        startPosition = b2body.getPosition();
        world.destroyBody(b2body);
        defineKnight();
        GroundEnemy.attackRange = 30;
    }

    public boolean isAttack(){
        return (currentState == State.ATTACKING1 || currentState == State.ATTACKING2);
    }

    public void hurtingCallBack(int dame){
        isHurt = true;
        knightHurtSound.play();

        health -= dame;
        if (health <= 0){
            health = 0;
            setDie();
            isHurt = false;
        }
    }

    public boolean isEndGame(){
        return endGame;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public int getDamage(){
        return damage;
    }

    public float getEnergy(){
        return untilCount;
    }

    public void setDie(){
        deathCount--;
        isDie = true;
        knightDieSound.play();
    }

    public void buff(){
        untilCount = UNTIL_COOL_DOWN;
    }

    public void attack1CallBack(){
        if (timeCountAttack > ATTACKING_COOL_DOWN){
            isAttacking1 = true;
            timeCountAttack = 0;
//            for (Enemy enemy : screen.creator.getGroundEnemies())
//                if (enemy.inRangeAttack && enemy.isUsable()) {
//                    enemy.hurtingCallBack();
//                }
        }
    }
    public void attack2CallBack(){
        if (timeCountAttack > ATTACKING_COOL_DOWN){
            isAttacking2 = true;
            timeCountAttack = 0;
//            for (Enemy enemy : screen.creator.getGroundEnemies())
//                if (enemy.inRangeAttack && enemy.isUsable()) {
//                    enemy.hurtingCallBack();
//                }
        }
    }
    public void attack3CallBack(){
        if (timeCountShoot > SHOOTING_COOL_DOWN) {
            isAttack3 = true;
            timeCountShoot = 0;
        }
    }

    public TextureRegion getFrame(float dt){
        timeCountShoot += dt;
        timeCountAttack += dt;
        if (untilCount < UNTIL_COOL_DOWN && !isBig) untilCount += dt;
        if (untilCount > UNTIL_COOL_DOWN && !isBig) untilCount = UNTIL_COOL_DOWN;
        //System.out.println("utilCount: " + untilCount);

        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case JUMPING:
                knightJumpSound.play();
                region = (TextureRegion) knightJump.getKeyFrame(stateTimer,false);
                break;
            case RUNNING:
                knightRunSound.play();
                region = (TextureRegion) knightRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING1:
                if (!playSound1){
                    playSound1 = true;
                    knightSwordSound.play();
                }
                region = (TextureRegion) knightAttack1.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING2:
                if (!playSound2){
                    playSound2 = true;
                    knightSwordSound.play();
                }
                region = (TextureRegion) knightAttack2.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING3://test
                region = (TextureRegion) knightAttack3.getKeyFrame(stateTimer, false);
                break;
            case DEAD:
                region = (TextureRegion) knightDie.getKeyFrame(stateTimer, false);
                break;
            case HURTING:
                region = (TextureRegion) knightHurt.getKeyFrame(stateTimer, false);
                break;
            case STANDING:
            default:
                region = (TextureRegion) knightStand.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()){
            region.flip(true, false);
            isRunningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()){
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = (currentState == previousState) ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        //die and hurt code
        if (isDie) {//test
            if (!knightDie.isAnimationFinished(stateTimer)) {
                return State.DEAD;
            }
            if (deathCount <= 0) endGame = true;
            else {
                b2body.setTransform(32 / GameWorld.PPM, 100 / GameWorld.PPM, 0);
                screen.getGameCam().position.set( screen.getGamePort().getWorldWidth() /2, screen.getGamePort().getWorldHeight() /2, 0);
                isDie = false;
                health = healthMax;
            }
        }

        if (isHurt) {
            isHurting = true;
            isAttacking1 = isAttacking2 = isAttacking3 = false;
            isHurt = false;
            //System.out.println("KNIGHT HURTTTTTTTT");
            return State.HURTING;
        }

        if (isHurting) {
            if (!knightHurt.isAnimationFinished(stateTimer)) {
                //System.out.println("KKKKKKKKKKKKKKKK");
                return State.HURTING;
            } else isHurting = false;
        }

        //attack code
        if (isAttack3){//avoid shooting without animation
            isAttack3 = false;
            isAttacking3 = true;
            return State.ATTACKING3;
        }
        if (isAttacking1){//test
            if (!knightAttack1.isAnimationFinished(stateTimer)){
                return State.ATTACKING1;
            }
            else {
                isAttacking1 = false;
                playSound1 = false;
            }
        }
        else if (isAttacking2){
            if (!knightAttack2.isAnimationFinished(stateTimer)){
                return State.ATTACKING2;
            }
            else {
                isAttacking2 = false;
                playSound2 = false;
            }
        }
        else if (isAttacking3){//TEST O DAY
            if (!knightAttack3.isAnimationFinished(stateTimer)){
                return State.ATTACKING3;
            }
            else {//create arrow
                int arrowDirection = (isRunningRight) ? 1 : -1;
                bulletManager.addBullet(b2body.getPosition().x, b2body.getPosition().y, arrowDirection, "Arrow");
                isAttacking3 = false;
                knightArrowSound.play();
            }
        }
        //movement code
        if (b2body.getLinearVelocity().y == 0 && previousState == State.JUMPING){
            b2body.setLinearVelocity(0, 0);//avoid sliding after jumping down
        }
        if (b2body.getLinearVelocity().y != 0){
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else return State.STANDING;
    }

    public int getHealth() {
        return health;
    }

    public void buffHealth(int add){
        health += add;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public void update(float dt){
        if (isBig) timeCountBig += dt;
        if (timeCountBig > BIG_TIMER){
            endBigMode();
            timeCountBig = 0;
        }

        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        TextureRegion frame = getFrame(dt);

        //this scale Knight 1.5 time bigger
        setBounds(getX(), getY(), frame.getRegionWidth()/GameWorld.PPM*scaleX,
            frame.getRegionHeight()/GameWorld.PPM*scaleY);
        setRegion(frame);
        bulletManager.update(dt);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        bulletManager.draw(batch);
    }
}
