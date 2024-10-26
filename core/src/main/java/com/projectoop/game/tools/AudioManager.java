package com.projectoop.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static AssetManager manager;

    public static String backgroundMusic = "Audio/GameMusic/Map2Music.mp3";
    public static String knightRunAudio = "Audio/SoundEffect/HeroRun.mp3";
    public static String knightHurtAudio = "Audio/SoundEffect/oizoioi.mp3";
    public static String knightJumpAudio = "Audio/SoundEffect/Jump.wav";

    public static void setUp(){
        manager = new AssetManager();
        manager.load(backgroundMusic, Music.class);

        manager.load(knightHurtAudio, Sound.class);
        manager.load(knightJumpAudio, Sound.class);
        manager.load(knightRunAudio, Sound.class);
        manager.finishLoading();
    }
}
