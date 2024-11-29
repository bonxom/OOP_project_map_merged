package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;


public class BulletManager {
    public Array<Bullet> bullets;

    public World world;
    private PlayScreen screen;

    public BulletManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;

        bullets = new Array<>();
    }

    public void addBullet(float x, float y, int direction, String name){
        Bullet bullet;
        switch (name){
            case "Arrow":
                bullet = new Arrow(screen, x, y, direction);
                break;
            case "FireBall":
                bullet = new FireBall(screen, x, y, direction);
                break;
            case "BossBall":
                bullet = new BossBall(screen, x, y, direction);
                break;
            default:
                bullet = null;
                break;
        }
        if (bullet != null) bullets.add(bullet);
    }

    public void update(float dt){
        Array<Bullet> removeBullets = new Array<>();
        for (Bullet bullet : bullets){
            bullet.update(dt);
            if (bullet.setToDestroy) {//mark for removal
                removeBullets.add(bullet);
            }
        }
        //debug();
        bullets.removeAll(removeBullets, true); //remove all marked bullet
    }

    public void draw(Batch batch){
        for (Bullet bullet : bullets){
            bullet.draw(batch);
        }
    }

    public void debug(){
        System.out.println(bullets.size);
        for (Bullet bullet : bullets){
            System.out.println("posX: " + bullet.getX() + " speed: " + bullet.velocity.x);
        }
    }

    public void dispose() {
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        bullets.clear();
    }
}
