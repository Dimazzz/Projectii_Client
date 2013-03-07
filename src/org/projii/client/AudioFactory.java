package org.projii.client;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

public class AudioFactory {

	public static Sound getSound(String pathToSound,Engine mEngine,BaseGameActivity activity)
	{
		Sound sound = null ;
		 SoundFactory.setAssetBasePath("mfx/");
	     try {
	         sound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), activity, pathToSound);
	     } catch (IllegalStateException e) {
	         e.printStackTrace();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }	
	     return sound;
	}
	public static Music getMusic(String pathToMusic,Engine mEngine,BaseGameActivity activity)
	{
		Music music = null;
		 MusicFactory.setAssetBasePath("mfx/");

	     try {
	         music = MusicFactory.createMusicFromAsset(mEngine
	             .getMusicManager(), activity, pathToMusic);
	         music.setLooping(true);
	     } catch (IllegalStateException e) {
	         e.printStackTrace();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
		return music;
	}
}
