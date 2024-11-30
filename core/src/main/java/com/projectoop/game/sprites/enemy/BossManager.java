package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.screens.PlayScreen;

public class BossManager {
    public World world;
    private PlayScreen screen;


    public BossManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;
    }

    public void addEnemy(float x, float y, String name) {
        GroundEnemy groundEnemy;
        switch (name) {
            case "Goblin":
                groundEnemy = new Goblin(screen, x, y);
                break;
            case "Orc":
                groundEnemy = new Orc(screen, x, y);
                break;
            case "Skeleton":
                groundEnemy = new Skeleton(screen, x, y);
                break;
            default:
                groundEnemy = null;
                break;
        }
        screen.creator.getGroundEnemies().add(groundEnemy);
    }
}

