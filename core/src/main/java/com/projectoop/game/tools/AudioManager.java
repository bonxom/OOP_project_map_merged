package com.projectoop.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AudioManager {
    public static AssetManager manager;
    //audio
    public static String backgroundMusic = "Audio/GameMusic/Map2Music.mp3";
    public static String knightRunAudio = "Audio/SoundEffect/HeroRun.mp3";
    public static String knightHurtAudio = "Audio/SoundEffect/oizoioi.mp3";
    public static String knightJumpAudio = "Audio/SoundEffect/Jump.wav";
    //animation
    public static Animation<TextureRegion> knightRun;
    public static Animation<TextureRegion> knightJump;
    public static Animation<TextureRegion> goombaWalk;


    public static void setUp(){
        manager = new AssetManager();
        manager.load(backgroundMusic, Music.class);

        manager.load(knightHurtAudio, Sound.class);
        manager.load(knightJumpAudio, Sound.class);
        manager.load(knightRunAudio, Sound.class);
        manager.finishLoading();
    }
}
