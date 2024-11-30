package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.BossHealthBar;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.weapons.BulletManager;
import com.projectoop.game.tools.AudioManager;

public class Boss extends Enemy{

    protected float scaleX;
    protected float scaleY;

    protected TextureAtlas atlasWalking;
    protected TextureAtlas atlasAttacking;
    protected TextureAtlas atlasHurting;
    protected TextureAtlas atlasDieing;

    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> hurtAnimation;
    protected Animation<TextureRegion> dieAnimation;

    protected Sound attackSound;
    protected Sound hurtSound;
    protected Sound dieSound;

    protected boolean playSoundAttack;
    protected BossHealthBar healthBar;
    protected float maxHealth = 500; // Ví dụ về máu tối đa
    protected float currentHealth;

    protected boolean lastDirectionIsRight;
    protected float addYtoAnim;

    private final float COOL_DOWN = 3;
    private float timeCount;
    private float lastTimeShoot;
    private BulletManager bulletManager;
    //  private Array<GroundEnemy> monsters;

    // private BossManager1 bossManager;
    private BossManager bossManager;
    public static float attackRange;

    private GroundEnemy.State currentState1;

    public Boss(PlayScreen screen, float x, float y, float addY, float scale, int damage) {
        super(screen, x, y);
        this.addYtoAnim = addY;
        this.scaleX = this.scaleY = scale;
        this.damage = damage;

        lastTimeShoot = 0;
        timeCount = 2;
        bulletManager = new BulletManager(screen);
        bossManager = new BossManager(screen);

        currentState = State.WALKING;
        previousState = State.WALKING;
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isAttack = false;
        isAttacking = false;
        isHurt = false;
        isHurting = false;
        isDie = false;
        inRangeAttack = false;
        playSoundAttack = false;
        currentHealth = maxHealth;
        healthBar = new BossHealthBar(this, maxHealth);

        attackRange = 30;


    }

    protected void prepareAnimation(){
        atlasWalking = new TextureAtlas("Boss/Pack/Walk.pack");
        atlasAttacking = new TextureAtlas("Boss/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("Boss/Pack/Death.pack");
        atlasHurting = new TextureAtlas("Boss/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.3f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }
    protected void prepareAudio(){
        attackSound = AudioManager.manager.get(AudioManager.orgAttackAudio, Sound.class);
        dieSound = AudioManager.manager.get(AudioManager.orgDieAudio, Sound.class);
        hurtSound = AudioManager.manager.get(AudioManager.orgHurtAudio, Sound.class);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(9/GameWorld.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(18 / GameWorld.PPM, 25 / GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT| GameWorld.PILAR_BIT | GameWorld.ARROW_BIT |
            GameWorld.KNIGHT_SWORD_LEFT | GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void hurtKnockBack() {
        double crTime = System.currentTimeMillis();


        double t = 200;
        if(System.currentTimeMillis()-crTime <t) {
            if(screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x)
                b2body.applyLinearImpulse(new Vector2(20,5), b2body.getWorldCenter(),true);
            else
                b2body.applyLinearImpulse(new Vector2(-20,5), b2body.getWorldCenter(),true);
        }

    }

    @Override
    public void destroy() {
        setToDestroy = true;
    }

    @Override
    public void attackingCallBack() {
        attackSound.play();
        isAttack = true;
//        System.out.println("Chem chem chem");
//        screen.getPlayer().hurtingCallBack();
    }
    //    public Boss.State getCurrentState(){
//        return currentState;
//    }
    @Override
    public void hurtingCallBack() {
        if (!destroyed) {
            hurtSound.play();
            hurtKnockBack();
            isHurt = true;

            //take dame
            currentHealth -= screen.getPlayer().getDamage();
            if (currentHealth < 0) currentHealth = 0;
            healthBar.update(currentHealth);
            if (currentHealth <= 0) {
                isDie = true;
            }
        }
    }

    public TextureRegion getFrame(float dt){
        timeCount += dt;
        System.out.println("TimeCount: " + timeCount);
        if(timeCount > COOL_DOWN) {
            //System.out.println("dcmmm");
            int direction = (runningRight) ? 1 : -1;

            bulletManager.addBullet(b2body.getPosition().x, b2body.getPosition().y+15/GameWorld.PPM, direction, "BossBall");
            int rnd1 = MathUtils.random(20);
            switch (rnd1) {
                case 0:
                    bossManager.addEnemy(b2body.getPosition().x, b2body.getPosition().y, "Skeleton");
                    break;
                case 1:
                    bossManager.addEnemy(b2body.getPosition().x, b2body.getPosition().y, "Orc");
                    break;
                case 2:
                    bossManager.addEnemy(b2body.getPosition().x, b2body.getPosition().y, "Goblin");
                    break;
                default:
                    break;
            }

//             = new Array<>();monsters
//           monsters.add(new Skeleton(screen, b2body.getPosition().x, b2body.getPosition().y));
            timeCount = 0;
        }
        currentState = getState();
        TextureRegion frame;


        switch (currentState){
            case DEAD:
                frame = (TextureRegion) dieAnimation.getKeyFrame(stateTime, false);
                //System.out.println("dead");
                break;
            case HURTING:
                frame = (TextureRegion) hurtAnimation.getKeyFrame(stateTime, false);
                //System.out.println("hurt");
                break;
            case ATTACKING:
                frame = (TextureRegion) attackAnimation.getKeyFrame(stateTime, false);
                //System.out.println("attack");
                break;
            case WALKING:
                //System.out.println("walk");
            default:
                frame = walkAnimation.getKeyFrame(stateTime, true);
        }
        if (currentState != State.ATTACKING) {
            if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !frame.isFlipX()){
                frame.flip(true, false);
                runningRight = false;
            }
            else if ((b2body.getLinearVelocity().x > 0 || runningRight) && frame.isFlipX()){
                frame.flip(true, false);
                runningRight = true;
            }}

        stateTime = (currentState == previousState) ? stateTime + dt : 0;
        previousState = currentState;
        return frame;
    }

    public State getState(){

        //die and hurt code
        if (isDie){
            isDie = false;
            isDieing = true;
            this.velocity.x = 0;
            return State.DEAD;
        }
        if (isDieing){
            if (dieAnimation.isAnimationFinished(stateTime)) {
                destroy();
            }
            //call destroy
            return State.DEAD;
        }

        if (isHurt){
            isHurt = false;
            isHurting = true;
            this.velocity.x = 0;
            return State.HURTING;
        }
        if(isHurting) {//test
            if(!hurtAnimation.isAnimationFinished(stateTime)) {
                //System.out.println("hihihihihihihh");
                return State.HURTING;
            }
            else {
                isHurting = false;
                this.velocity.x = runningRight ? 1 : -1;
            }
        }
        //attack code
        if (isAttack){
            isAttacking = true;
            isAttack = false;
            this.velocity.x = 0;
            return State.ATTACKING;
        }
        if (isAttacking){//test
            if (!attackAnimation.isAnimationFinished(stateTime)){
                //System.out.println("attacking");
                return State.ATTACKING;
            }
            else {
                isAttacking = false;
                this.velocity.x = runningRight ? 1 : -1;
                //playSound1 = false;
            }
        }

        return State.WALKING;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            //world.destroyBody(b2body);
            destroyed = true;
            b2body.setTransform(0, -10, 0);
            stateTime = 0;
        }
        else if (!destroyed){
            Vector2 playerPosition = screen.getPlayer().b2body.getPosition();
            float speed = 1.0f; // Tốc độ di chuyển của Boss
            if ((b2body.getPosition().x+200/GameWorld.PPM < playerPosition.x) && runningRight == false) {
                velocity.x = speed; // Di chuyển sang phải
                runningRight = true;
            } else if((b2body.getPosition().x-200/GameWorld.PPM > playerPosition.x) && runningRight == true) {
                velocity.x = -speed; // Di chuyển sang trái
                runningRight = false;
            }
            TextureRegion frame = getFrame(dt);
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            //this y + addY to move the animation stand on the ground
            setBounds(getX(), getY()+ addYtoAnim/GameWorld.PPM, frame.getRegionWidth() / GameWorld.PPM * scaleX,
                frame.getRegionHeight() / GameWorld.PPM * scaleY);
            setRegion(frame);
        }
        System.out.println(velocity.x);
        bulletManager.update(dt);
    }


    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1){
            super.draw(batch);
            healthBar.draw(batch);
        }
        bulletManager.draw(batch);

    }
    //    public void takeDamage(float damage) {
//        currentHealth -= damage;
//        healthBar.update(currentHealth);
//        if(currentHealth<=0) destroy();
//    }
    @Override
    public void hitOnHead() {
        //    takeDamage(50);
    }

}
