package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectoop.game.screens.PlayScreen;

public class FlyEnemy extends GroundEnemy{//test th, code sau
    public FlyEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y, 14, 1);
    }

    @Override
    protected void prepareAnimation() {
        atlasWalking = new TextureAtlas("E_FlyingEye/Pack/Flight.pack");
        atlasAttacking = new TextureAtlas("E_FlyingEye/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("E_FlyingEye/Pack/Death.pack");
        atlasHurting = new TextureAtlas("E_FlyingEye/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.3f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }
}
