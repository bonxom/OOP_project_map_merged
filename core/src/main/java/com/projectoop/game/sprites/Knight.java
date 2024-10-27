package com.projectoop.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

public class Knight extends Sprite {
    public enum State {FALLING, HURTING, ATTACKING1, ATTACKING2, ATTACKING3, DIEING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;

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
    private Sound knightAttackSound;
    private Sound knightHurtSound;
    private Sound knightDieSound;

    private float stateTimer;
    private boolean runningRight;


    public Knight(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        System.out.println("Hello baby");
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        prepareAnimation();
        prepareSound();
        defineKnight();

        setBounds(0, 0, 16/GameWorld.PPM, 16/GameWorld.PPM);
    }

    private void prepareAnimation(){
        atlasRunning = new TextureAtlas("KnightAsset/Pack/Run.pack");
        atlasJumping = new TextureAtlas("KnightAsset/Pack/Attack02.pack");
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
        knightAttack1 = new Animation<TextureRegion>(0.5f, atlasAttacking1.getRegions());
        knightAttack2 = new Animation<TextureRegion>(0.05f, atlasAttacking2.getRegions());
        knightAttack3 = new Animation<TextureRegion>(0.05f, atlasAttacking3.getRegions());
        knightHurt = new Animation<TextureRegion>(0.5f, atlasBeingHurt.getRegions());
    }

    private void prepareSound(){
        knightJumpSound = AudioManager.manager.get(AudioManager.knightJumpAudio, Sound.class);
        knightRunSound = AudioManager.manager.get(AudioManager.knightRunAudio, Sound.class);
        knightHurtSound = AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class);
    }

    public void defineKnight(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/GameWorld.PPM, 100/GameWorld.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.KNIGHT_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | GameWorld.SPIKE_BIT |
            GameWorld.LAVA_BIT | GameWorld.ENEMY_BIT | GameWorld.OBJECT_BIT| GameWorld.ENEMY_HEAD_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //make EdgeShape for checking head-collision
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/GameWorld.PPM, 6/GameWorld.PPM),
                new Vector2(2/GameWorld.PPM, 6/GameWorld.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");

        //make EdgeShape for checking foot-collision
        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-2/GameWorld.PPM, -6/GameWorld.PPM),
                new Vector2(2/GameWorld.PPM, -6/GameWorld.PPM));
        fdef.shape = foot;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("foot");
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        TextureRegion frame = getFrame(dt);
        //this also scale Knight 1.5 time bigger
        if (currentState == State.RUNNING) {
            setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM * 1.5f, frame.getRegionHeight() / GameWorld.PPM * 1.5f);
        } else {
            setBounds(getX(), getY(), frame.getRegionWidth() * 1.5f/ GameWorld.PPM, frame.getRegionHeight() / GameWorld.PPM * 1.5f);
        }
        setRegion(frame);
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case JUMPING:
                knightJumpSound.play();
                region = (TextureRegion) knightJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                knightRunSound.play();
                region = (TextureRegion) knightRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING1:
                region = (TextureRegion) knightAttack1.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING2:
                region = (TextureRegion) knightAttack2.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING3:
                region = (TextureRegion) knightAttack3.getKeyFrame(stateTimer, true);
                break;
            case DIEING:
                region = (TextureRegion) knightDie.getKeyFrame(stateTimer, true);
                break;
            case HURTING:
                region = (TextureRegion) knightHurt.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) knightStand.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = (currentState == previousState) ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().y < 0){
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else return State.STANDING;
    }
}
