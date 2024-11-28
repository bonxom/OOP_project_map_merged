package com.projectoop.game.scences;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;

public class PlayerHealthBar {
    private Texture HeroHealthBar,HeroHealth;
    private Texture BossHealthBar,BossHealth;
    private PlayScreen screen;
    private TextureRegion currentRank;
    SpriteBatch batch;

    public PlayerHealthBar(PlayScreen x) {
        HeroHealthBar = new Texture("HealthBar/bg.png");
        HeroHealth =  new Texture("HealthBar/red.png");


        //get batch and player
        this.screen = x;
        batch = x.game.batch;
    }

    public void draw(float dt) {

        // draw Hero HeroHeroHealthBar
        float x = screen.getGameCam().position.x-screen.getGamePort().getWorldWidth()-100/ GameWorld.PPM;
        float y = 175/ GameWorld.PPM+screen.getGameCam().position.y+screen.getGamePort().getWorldHeight()/2;



        batch.draw(HeroHealthBar, x+3,y-2,HeroHealthBar.getWidth()/  GameWorld.PPM/2,HeroHealthBar.getHeight()/  GameWorld.PPM/2, 0,0,HeroHealthBar.getWidth(),HeroHealthBar.getHeight(),false,false);

        float ratio = 1.0f*screen.getPlayer().getHealth()/screen.getPlayer().getHealthMax();
        float HealthX = x+3+(HeroHealthBar.getWidth()-HeroHealth.getWidth())/2/  GameWorld.PPM;
        float HealthY = y-2+(HeroHealthBar.getHeight()-HeroHealth.getHeight())/2/  GameWorld.PPM;
        // draw at HeroHealthX and HeroHealthY with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height) (!!!) and scale with PPM
        // and take portion of Texuture with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height)


        batch.draw(HeroHealth, HealthX,HealthY,
            (HeroHealth.getWidth()/  GameWorld.PPM/2)*ratio,HeroHealth.getHeight()/  GameWorld.PPM/2,
            0,0,(int)(HeroHealth.getWidth()*ratio),HeroHealth.getHeight(),
            false,false);


    }

}
