package com.projectoop.game.scences;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.screens.ThirdMapScreen;

public class PlayerEnergyBar {
    private Texture heroEnergyBar,heroEnergy;
    private PlayScreen screen;
    SpriteBatch batch;

    public PlayerEnergyBar(PlayScreen x) {
        heroEnergyBar = new Texture("HealthBar/bg.png");
        heroEnergy =  new Texture("HealthBar/blue.png");

        //get batch and player
        this.screen = x;
        batch = x.game.batch;
    }

    public void draw(float dt) {

        // draw Hero HeroHeroHealthBar
        float x = screen.getGameCam().position.x-screen.getGamePort().getWorldWidth()-100/ GameWorld.PPM;
        float y = 155/ GameWorld.PPM+screen.getGameCam().position.y+screen.getGamePort().getWorldHeight()/2;



        batch.draw(heroEnergyBar, x+3,y-2, heroEnergyBar.getWidth()/  GameWorld.PPM/2, heroEnergyBar.getHeight()/  GameWorld.PPM/2, 0,0, heroEnergyBar.getWidth(), heroEnergyBar.getHeight(),false,false);

        float ratio = 1.0f*screen.getPlayer().getEnergy()/screen.getPlayer().UNTIL_COOL_DOWN;
        float HealthX = x+3+(heroEnergyBar.getWidth()-heroEnergy.getWidth())/2/  GameWorld.PPM;
        float HealthY = y-2+(heroEnergyBar.getHeight()-heroEnergy.getHeight())/2/  GameWorld.PPM;
        // draw at HeroHealthX and HeroHealthY with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height) (!!!) and scale with PPM
        // and take portion of Texuture with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height)


        batch.draw(heroEnergy, HealthX,HealthY,
            (heroEnergy.getWidth()/  GameWorld.PPM/2)*ratio,heroEnergy.getHeight()/  GameWorld.PPM/2,
            0,0,(int)(heroEnergy.getWidth()*ratio),heroEnergy.getHeight(),
            false,false);

    }
}
