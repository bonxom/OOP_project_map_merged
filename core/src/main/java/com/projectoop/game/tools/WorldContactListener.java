package com.projectoop.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.effectedObject.Chest;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.sprites.items.Potion;
import com.projectoop.game.sprites.trap.InteractiveTileObject;
import com.projectoop.game.sprites.trap.Spike;
import com.projectoop.game.sprites.weapons.Arrow;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("Begin Contact", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

//        if (fixA.getUserData() == "head" || fixB.getUserData() == "head"){
//            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//            //System.out.println("Head hit 1");
//
//            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onHeadHit();
//                //System.out.println("Head hit 2");
//            }
//        }
//
//        if (fixA.getUserData() == "foot" || fixB.getUserData() == "foot"){
//            Fixture head = fixA.getUserData() == "foot" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//
//            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onFootHit();
//
//            }
//        }

        switch (cDef){
            //trap collision
            case GameWorld.KNIGHT_FOOT_BIT | GameWorld.LAVA_BIT:
            case GameWorld.KNIGHT_FOOT_BIT | GameWorld.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.KNIGHT_FOOT_BIT){
                    ((InteractiveTileObject) fixB.getUserData()).onFootHit((Knight) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).onFootHit((Knight) fixB.getUserData());
                }
                break;
            //enemy collision
            case GameWorld.ENEMY_BIT | GameWorld.PILAR_BIT://enemy collide with object -> reverse
                Gdx.app.log("Enemy", "Pilar");
                Enemy enemy = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());
                enemy.reverseVelocity(true, false);
                break;
            case GameWorld.ENEMY_BIT | GameWorld.ENEMY_BIT:
                Gdx.app.log("Orc", "Orc");
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            //arrow collision
            case GameWorld.ENEMY_BIT | GameWorld.ARROW_BIT://test
                Gdx.app.log("Arrow", "Enemy");
                Arrow arrow = (Arrow) ((fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) ? fixA.getUserData() : fixB.getUserData());
                arrow.destroy();
                break;
            case GameWorld.GROUND_BIT | GameWorld.ARROW_BIT:
            case GameWorld.SPIKE_BIT | GameWorld.ARROW_BIT:
            case GameWorld.LAVA_BIT | GameWorld.ARROW_BIT:
            case GameWorld.OBJECT_BIT | GameWorld.ARROW_BIT:
                Gdx.app.log("Arrow", "Object");
                Arrow arrow1 = (Arrow) ((fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) ? fixA.getUserData() : fixB.getUserData());
                arrow1.destroy();
                break;
            //chest collision
            case GameWorld.CHEST_BIT | GameWorld.KNIGHT_FOOT_BIT:
            case GameWorld.CHEST_BIT | GameWorld.KNIGHT_BIT:
                Gdx.app.log("Knight", "Open Chest");
                Chest chest = (Chest)((fixA.getFilterData().categoryBits == GameWorld.CHEST_BIT) ? fixA.getUserData() : fixB.getUserData());
                chest.usingCallBack();
                break;
            case GameWorld.ITEM_BIT | GameWorld.KNIGHT_BIT:
                Gdx.app.log("Knight", "Buff");
                if(fixA.getFilterData().categoryBits == GameWorld.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Knight) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Knight) fixA.getUserData());
                break;
            //knight and enemy
            case GameWorld.KNIGHT_BIT | GameWorld.ENEMY_BIT:
                Gdx.app.log("Knight", "hurt_enemy");
                Knight knight = (Knight)((fixA.getFilterData().categoryBits == GameWorld.KNIGHT_BIT) ? fixA.getUserData() : fixB.getUserData());
                //enemy.hit();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
