package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;

public class Goblin extends GroundEnemy{

    public Goblin(PlayScreen screen, float x, float y) {
        super(screen, x, y, 7, 1.2f, 10);
    }

    @Override
    protected void prepareAnimation() {
        atlasWalking = new TextureAtlas("E_Goblin/Pack/Walk.pack");
        atlasAttacking = new TextureAtlas("E_Goblin/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("E_Goblin/Pack/Death.pack");
        atlasHurting = new TextureAtlas("E_Goblin/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.1f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }

    @Override
    public void dispose() {

    }
}
