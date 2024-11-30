package com.projectoop.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.effectedObject.Chest;
import com.projectoop.game.sprites.effectedObject.Chest1;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.trap.InteractiveTileObject;
import com.projectoop.game.sprites.trap.Portal;
import com.projectoop.game.sprites.weapons.Arrow;
import com.projectoop.game.sprites.weapons.BossBall;
import com.projectoop.game.sprites.weapons.FireBall;

public class WorldContactListener implements ContactListener {
    private PlayScreen screen;

    public WorldContactListener(PlayScreen playScreen) {
        this.screen = playScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.log("Begin Contact", "");
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
            case GameWorld.KNIGHT_SWORD_RIGHT | GameWorld.PORTAL_BIT:
                Gdx.app.log("knight", "portal");
                if (fixA.getFilterData().categoryBits == GameWorld.KNIGHT_SWORD_RIGHT){
                    ((InteractiveTileObject) fixB.getUserData()).passThisRound((Knight) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).passThisRound((Knight) fixB.getUserData());
                }
                break;
            case GameWorld.KNIGHT_FOOT_BIT | GameWorld.TRAP_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.KNIGHT_FOOT_BIT){
                    ((InteractiveTileObject) fixB.getUserData()).onFootHit((Knight) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).onFootHit((Knight) fixB.getUserData());
                }
                break;
            //enemy collision
            case GameWorld.ENEMY_BIT | GameWorld.PILAR_BIT://enemy collide with object -> reverse
                //Gdx.app.log("Enemy", "Pilar");
                Enemy enemy = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());
                enemy.reverseVelocity(true, false);
                break;
            //arrow collision
            case GameWorld.ENEMY_BIT | GameWorld.ARROW_BIT://test
                //Gdx.app.log("Arrow", "Enemy");
                if(fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixA.getUserData())).destroy();
                    ((Enemy)(fixB.getUserData())).hurtingCallBack();
                }
                else {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixB.getUserData())).destroy();
                    ((Enemy)(fixA.getUserData())).hurtingCallBack();
                }
                break;
            case GameWorld.GROUND_BIT | GameWorld.ARROW_BIT:
            case GameWorld.CHEST1_BIT | GameWorld.ARROW_BIT:
            case GameWorld.CHEST_BIT | GameWorld.ARROW_BIT:
                //Gdx.app.log("Arrow", "Object");
                Arrow arrow1 = (Arrow) ((fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) ? fixA.getUserData() : fixB.getUserData());
                arrow1.destroy();
                break;
            //fireball collision
            case GameWorld.KNIGHT_BIT | GameWorld.FIREBALL_BIT://test
                //Gdx.app.log("Arrow", "Enemy");
                FireBall fireBall = (FireBall) ((fixA.getFilterData().categoryBits == GameWorld.FIREBALL_BIT) ? fixA.getUserData() : fixB.getUserData());
                fireBall.destroy();
                screen.getPlayer().hurtingCallBack(fireBall.damage);
                break;
            case GameWorld.GROUND_BIT | GameWorld.FIREBALL_BIT:
            case GameWorld.CHEST_BIT | GameWorld.FIREBALL_BIT:
            case GameWorld.CHEST1_BIT | GameWorld.FIREBALL_BIT:
                //Gdx.app.log("Arrow", "Object");
                FireBall fireBall1 = (FireBall) ((fixA.getFilterData().categoryBits == GameWorld.FIREBALL_BIT) ? fixA.getUserData() : fixB.getUserData());
                fireBall1.destroy();
                break;
            //bossball collision
            case GameWorld.KNIGHT_BIT | GameWorld.BOSSBALL_BIT://test
                //Gdx.app.log("Arrow", "Enemy");
                BossBall bossBall = (BossBall) ((fixA.getFilterData().categoryBits == GameWorld.BOSSBALL_BIT) ? fixA.getUserData() : fixB.getUserData());
                bossBall.destroy();
                screen.getPlayer().hurtingCallBack(bossBall.damage);
                break;
            case GameWorld.GROUND_BIT | GameWorld.BOSSBALL_BIT:
            case GameWorld.CHEST_BIT | GameWorld.BOSSBALL_BIT:
            case GameWorld.CHEST1_BIT | GameWorld.BOSSBALL_BIT:
                //Gdx.app.log("Arrow", "Object");
                BossBall bossBall1 = (BossBall) ((fixA.getFilterData().categoryBits == GameWorld.FIREBALL_BIT) ? fixA.getUserData() : fixB.getUserData());
                bossBall1.destroy();
                break;

            //chest collision
            case GameWorld.CHEST_BIT | GameWorld.KNIGHT_BIT:
                //Gdx.app.log("Knight", "Open Chest");
                Chest chest = (Chest)((fixA.getFilterData().categoryBits == GameWorld.CHEST_BIT) ? fixA.getUserData() : fixB.getUserData());
                chest.usingCallBack();
                break;
            case GameWorld.CHEST1_BIT | GameWorld.KNIGHT_BIT:
                Chest1 chest1 = (Chest1)((fixA.getFilterData().categoryBits == GameWorld.CHEST1_BIT) ? fixA.getUserData() : fixB.getUserData());
                chest1.usingCallBack();
                break;
            case GameWorld.ITEM_BIT | GameWorld.KNIGHT_BIT:
                //Gdx.app.log("Knight", "Buff");
                if(fixA.getFilterData().categoryBits == GameWorld.ITEM_BIT) {
                    ((Item) fixA.getUserData()).use((Knight) fixB.getUserData());
                    screen.itemsToRemove.add((Item) fixA.getUserData());
                }
                else {
                    ((Item) fixB.getUserData()).use((Knight) fixA.getUserData());
                    screen.itemsToRemove.add((Item) fixB.getUserData());
                }
                break;
            //knight and enemy
//            case GameWorld.KNIGHT_BIT | GameWorld.ENEMY_BIT:
//                if(fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) {
//                    Gdx.app.log("Enemy hit", "");
//                    ((Enemy) fixA.getUserData()).attackingCallBack();
//                }
//                else {
//                    Gdx.app.log("Enemy hit", "");
//                    ((Enemy) fixB.getUserData()).attackingCallBack();
//                    //((Knight) fixA.getUserData()).hurtingCallBack();
//                }
//                break;
            // Sword sensor collisions
            case GameWorld.KNIGHT_SWORD_RIGHT | GameWorld.ENEMY_BIT:
                Enemy enemyRight = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());

                if (enemyRight.velocity.x < 0){
                    enemyRight.attackingCallBack();
                    screen.getPlayer().hurtingCallBack(enemyRight.getDame());
                }
                //screen.getPlayer().monsterInRange.add(enemyRight);
                //enemyRight.hurtingCallBack();
                enemyRight.inRangeAttack = true;
                break;

            case GameWorld.KNIGHT_SWORD_LEFT | GameWorld.ENEMY_BIT:
                Enemy enemyLeft = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());

                if (enemyLeft.velocity.x > 0){
                    enemyLeft.attackingCallBack();
                    screen.getPlayer().hurtingCallBack(enemyLeft.getDame());
                }
                //screen.getPlayer().monsterInRange.add(enemyLeft);
                //enemyLeft.hurtingCallBack();
                enemyLeft.inRangeAttack = true;
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("End Contact", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case GameWorld.KNIGHT_SWORD_RIGHT | GameWorld.ENEMY_BIT:
                Enemy enemyRight = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());
                enemyRight.inRangeAttack = false;
                break;

            case GameWorld.KNIGHT_SWORD_LEFT | GameWorld.ENEMY_BIT:
                Enemy enemyLeft = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());
                enemyLeft.inRangeAttack = false;
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
