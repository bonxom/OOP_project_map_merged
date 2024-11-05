package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.screens.PlayScreen;

import java.util.ArrayList;

public class BulletManager {
    public ArrayList<Arrow> bullets;

    public World world;
    private PlayScreen screen;

    public BulletManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;

        bullets = new ArrayList<>();
    }

    public void addBullet(float x, float y, int direction){
        Arrow bullet = new Arrow(screen, x, y, direction);
        bullets.add(bullet);
    }

    public void update(float dt){
        ArrayList<Arrow> removeBullets = new ArrayList<>();
        for (Arrow bullet : bullets){
            bullet.update(dt);
            if (bullet.setToDestroy){//mark for removal
                removeBullets.add(bullet);
            }
        }
        bullets.removeAll(removeBullets); //remove all marked bullet
    }

    public void draw(Batch batch){
        for (Arrow bullet : bullets){
            bullet.draw(batch);
        }
    }

    public void debug(){
        System.out.println(bullets.size());
        for (Arrow bullet : bullets){
            System.out.println("posX: " + bullet.getX());
        }
    }

    public void dispose() {
        for (Arrow bullet : bullets) {
            bullet.dispose();
        }
        bullets.clear();
    }
}
