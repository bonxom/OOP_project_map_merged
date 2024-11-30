package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;

public class Orc extends GroundEnemy{

    public Orc(PlayScreen screen, float x, float y) {
        super(screen, x, y, -2, 2, 10);
    }

    @Override
    protected void prepareAnimation(){
        atlasWalking = new TextureAtlas("E_Orc/Pack/Walk.pack");
        atlasAttacking = new TextureAtlas("E_Orc/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("E_Orc/Pack/Die.pack");
        atlasHurting = new TextureAtlas("E_Orc/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.3f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }

    @Override
    public void dispose() {

    }
}
