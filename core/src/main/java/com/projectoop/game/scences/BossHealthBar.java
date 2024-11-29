package com.projectoop.game.scences;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.projectoop.game.GameWorld;
import com.projectoop.game.sprites.enemy.FlyEnemy;
import com.projectoop.game.sprites.enemy.Boss;

public class BossHealthBar {
    private Texture bgTexture;
    private Texture redTexture;
    private float maxHealth;
    private float currentHealth;
    private Boss boss;

    public BossHealthBar(Boss boss, float maxHealth) {
        this.boss = boss;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        bgTexture = new Texture("HealthBar/bg.png");
        redTexture = new Texture("HealthBar/green.png");
    }

    public void update(float health) {
        this.currentHealth = health;
    }

    public void draw(Batch batch) {
        float healthPercentage = currentHealth / maxHealth;
        float barWidth = 60 / GameWorld.PPM;
        float barHeight = 8 / GameWorld.PPM;

        // Vị trí thanh máu ở trên đầu orc
//        float barX = groundEnemy.getX()+45/GameWorld.PPM;
//        float barY = groundEnemy.getY() + groundEnemy.getHeight() / 2 + barHeight+ 20/ GameWorld.PPM;
        float barX = boss.getX() + boss.getWidth() / 2 / GameWorld.PPM + barWidth-40/GameWorld.PPM;
        float barY = boss.getY() + boss.getHeight() / GameWorld.PPM + barHeight + 70/ GameWorld.PPM;
//        if (groundEnemy instanceof FlyEnemy)
//            barY += 13/GameWorld.PPM;
//        else
//            barY += 25/GameWorld.PPM;




        // Vẽ nền thanh máu
        batch.draw(bgTexture, barX, barY, barWidth, barHeight);

        // Vẽ thanh máu đỏ
        batch.draw(redTexture, barX, barY, barWidth * healthPercentage, barHeight);
    }

    public void dispose() {
        bgTexture.dispose();
        redTexture.dispose();
    }
}


