package com.projectoop.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.enemy.Goblin;
import com.projectoop.game.sprites.enemy.GroundEnemy;
import com.projectoop.game.sprites.enemy.Orc;
import com.projectoop.game.sprites.effectedObject.Chest;
import com.projectoop.game.sprites.trap.Pilar;
import com.projectoop.game.sprites.trap.Trap;

public class B2WorldCreator {
    private Array<GroundEnemy> groundEnemies;
    private Array<Chest> chests;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground_ however if we need to make it short
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ GameWorld.PPM, (rect.getY() + rect.getHeight()/2)/GameWorld.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/GameWorld.PPM, rect.getHeight()/2/GameWorld.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GameWorld.GROUND_BIT;//if (automatic) enemy collide with object, then change direction
            body.createFixture(fdef);
        }
        //trap
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            new Trap(screen, object);
        }
        //pilar
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            new Pilar(screen, object);
        }
        //create all orcs
//        orcs = new Array<>();
//        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)object).getRectangle();
//            orcs.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
//        }
        groundEnemies = new Array<>();
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            groundEnemies.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all goblins
        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            groundEnemies.add(new Goblin(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all chests
        chests = new Array<>();
        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            chests.add(new Chest(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
    }

    public Array<GroundEnemy> getGroundEnemies() {
        return groundEnemies;
    }

    public Array<Chest> getChests(){
        return chests;
    }
}
