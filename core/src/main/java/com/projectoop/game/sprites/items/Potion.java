package com.projectoop.game.sprites.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Potion extends Item{
    private Texture texture;
    private TextureRegion frame;

    public Potion(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        prepareAnimation();
        //the chest throw a potion with this velocity
        velocity = new Vector2(1.5f, 0.5f);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.ITEM_BIT;
        fdef.filter.maskBits = GameWorld.KNIGHT_BIT |
            GameWorld.OBJECT_BIT |
            GameWorld.GROUND_BIT |
            GameWorld.CHEST_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void prepareAnimation() {
        texture = new Texture("Props/Castle_Tileset_Potions-HP.png");
        frame = new TextureRegion(texture);
        setRegion(frame);
    }

    @Override
    public void use() {
        destroy();
    }

    @Override
    public void update(float dt){
        super.update(dt);
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
        //when y = 0, set the potion stop
        if (velocity.y == 0) body.setLinearVelocity(0, 0);
    }
}
