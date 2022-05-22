package com.jb.main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private Clip clip;

	public static final Sound musicBG = new Sound("graphics/res/music.wav");
	public static final Sound hurt = new Sound("graphics/res/hurt.wav");

	public Sound(String path) {
		try {
			File musicPath = new File(path);
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				clip = AudioSystem.getClip();
				clip.open(audioInput);
			} else {
				System.out.println("Can't find music file");
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}

	public void play() {
		clip.setFramePosition(0);
		clip.start();
		clip.drain();
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
		clip.close();
	}
}
