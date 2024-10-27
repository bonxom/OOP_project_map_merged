package com.projectoop.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.trap.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
//    public boolean isContact(short id1, short id2, Contact contact) {
//
//        Fixture x = contact.getFixtureA();
//        Fixture y = contact.getFixtureB();
//        if (x==null||y==null) return false;
//
//        short xx = x.getFilterData().categoryBits;
//        short yy = y.getFilterData().categoryBits;
//
//        if(xx == id1 && yy == id2) return true;
//        if(xx == id2 && yy == id1) return true;
//        return false;
//
//    }
    @Override
    public void beginContact(Contact contact) {
        //System.out.println("Hehe");
        Gdx.app.log("Begin Contact", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            //System.out.println("Head hit 1");

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
                //System.out.println("Head hit 2");
            }
        }

        if (fixA.getUserData() == "foot" || fixB.getUserData() == "foot"){
            Fixture head = fixA.getUserData() == "foot" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onFootHit();
            }
        }
//        if(isContact(GameWorld.KNIGHT_BIT,GameWorld.SPIKE_BIT,contact)){
//            System.out.println("Head hit");
//        }
        switch (cDef){
            case GameWorld.ENEMY_HEAD_BIT | GameWorld.KNIGHT_BIT://knight collide with enemy's head
                if (fixA.getFilterData().categoryBits == GameWorld.ENEMY_HEAD_BIT) {//A is enemy
                    ((Enemy) fixA.getUserData()).hitOnHead();
                }
                else{//B is enemy
                    ((Enemy) fixB.getUserData()).hitOnHead();
                }
                 break;
            case GameWorld.ENEMY_BIT | GameWorld.OBJECT_BIT://enemy collide with object -> reverse velocity
                System.out.println("Goomba hit the pilar");
                if (fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT){
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                else{
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case GameWorld.KNIGHT_BIT | GameWorld.ENEMY_BIT:
                Gdx.app.log("Knight", "die");
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
