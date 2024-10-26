package com.projectoop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameWorld extends Game {
    public static final int V_WIDTH = 400; //virtual width
    public static final int V_HEIGHT = 256;
    public static final float PPM = 100;//pixel per meter

    public static final short DEFAULT_BIT = 1;
    public static final short KNIGHT_BIT = 2;
    public static final short SPIKE_BIT = 4;
    public static final short LAVA_BIT = 8;
    public static final short DESTROYED_BIT = 16;

    public SpriteBatch batch;//container hold all images or texture for rendering


    @Override
    public void create() {
        AudioManager.setUp();
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        AudioManager.manager.dispose();
    }
}
