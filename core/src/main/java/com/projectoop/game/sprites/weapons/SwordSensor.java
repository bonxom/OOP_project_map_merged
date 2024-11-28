package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;

public class SwordSensor extends Sprite {
    public World world;
    public TiledMap map;
    public TiledMapTile tile;
    public Rectangle bounds;
    public Body body;

    public Fixture fixture;

    public SwordSensor(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX()+bounds.getWidth()/2)/ GameWorld.PPM,(bounds.getY()+bounds.getHeight()/2)/GameWorld.PPM);
        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2/GameWorld.PPM, bounds.getHeight()/2/GameWorld.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;

        fixture = body.createFixture(fdef);
    }
}
