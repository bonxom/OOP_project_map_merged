package com.projectoop.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

public class Knight extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion knightStand;
    private Animation<TextureRegion> knightRun;
    private Animation<TextureRegion> knightJump;

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

        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < 7; i++){
            frames.add(new TextureRegion(getTexture(), 1 + i * 16, 11, 16, 16));
        }
        knightRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(), 1 + i*16, 11, 16, 16));
        }
        knightJump = new Animation<TextureRegion>(0.1f, frames);

        defineKnight();
        knightStand = new TextureRegion(getTexture(), 1, 11, 16, 16);
        setBounds(0, 0, 16/GameWorld.PPM, 16/GameWorld.PPM);
        setRegion(knightStand);
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
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                AudioManager.manager.get(AudioManager.knightJumpAudio, Sound.class).play();
                region = (TextureRegion) knightJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                AudioManager.manager.get(AudioManager.knightRunAudio, Sound.class).play();
                region = (TextureRegion) knightRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = knightStand;
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
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else return State.STANDING;
    }
}
