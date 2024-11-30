package com.projectoop.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.*;
import com.projectoop.game.sprites.effectedObject.Chest1;
import com.projectoop.game.sprites.enemy.*;
import com.projectoop.game.sprites.effectedObject.Chest;
import com.projectoop.game.sprites.trap.Pilar;
import com.projectoop.game.sprites.trap.Portal;
import com.projectoop.game.sprites.trap.Trap;

public class B2WorldCreator {
    private Array<Enemy> groundEnemies;
    private Array<Chest> chests;
    private Array<Chest1> chest1s;
    public Portal portal2;
    public Portal portal3;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        ///////////Map của HMD
        if (screen instanceof ThirdMapScreen) {
            //ground_ however if we need to make it short
            for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / GameWorld.PPM, (rect.getY() + rect.getHeight() / 2) / GameWorld.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / GameWorld.PPM, rect.getHeight() / 2 / GameWorld.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = GameWorld.GROUND_BIT;//if (automatic) enemy collide with object, then change direction
                body.createFixture(fdef);
            }
            //trap
            for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
                new Trap(screen, object);
            }
            //pilar
            for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
                new Pilar(screen, object);
            }
            //list of enemies
            groundEnemies = new Array<>();
            //create all skeleton
            for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                groundEnemies.add(new Mushroom(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all goblins
            for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                groundEnemies.add(new Goblin(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all flyenemies;
            for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                groundEnemies.add(new FlyEnemy(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all chests
            chests = new Array<>();
            for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                chests.add(new Chest(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }

            for (MapObject object : map.getLayers().get(16).getObjects().getByType(RectangleMapObject.class)) {
                portal3 = new Portal(screen, object);
            }
        }


        //////////////////////////////////////////map của chính ngu
        else if (screen instanceof SecondMapScreen){
            for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
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
            for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
                new Trap(screen, object);
            }

            // crete pipes
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                new Pilar(screen, object);
            }
            //list of enemies
            groundEnemies = new Array<>();
            //create all skeleton
            for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                groundEnemies.add(new Skeleton(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all orcs
            for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                groundEnemies.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }

            //create all flyenemies;
            for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                groundEnemies.add(new FlyEnemy(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all chests
            chests = new Array<>();
            for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                chests.add(new Chest(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all chest1s
            chest1s = new Array<>();
            for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                chest1s.add(new Chest1(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }

            for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
                portal2 = new Portal(screen, object);
            }
        }

        /////////////////////map của 4 là
        else if (screen instanceof FourthMapScreen){
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
                //  groundEnemies.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
                groundEnemies.add(new Skeleton(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all goblins
//        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)object).getRectangle();
//            groundEnemies.add(new Goblin(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
//        }
            for (MapObject object : map.getLayers().get(17).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                groundEnemies.add(new FlyEnemy(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            //create all chests
            chests = new Array<>();
            for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                chests.add(new Chest(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }
            chest1s = new Array<>();
            for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                chest1s.add(new Chest1(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
            }


            for (MapObject object : map.getLayers().get(16).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                //  groundEnemies.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
                groundEnemies.add(new Boss(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM,35,1.5f,15));
            }
        }
    }



    public Array<Enemy> getGroundEnemies() {
        return groundEnemies;
    }

    public Array<Chest> getChests(){
        return chests;
    }

    public Array<Chest1> getChest1s(){
        return chest1s;
    }
}
