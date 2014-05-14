package com.example.gamedemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class GameScore {

	private int Score = 0;
	private BitmapFont font;
	private float x = 0;

	public GameScore(ImageButton pause) {
		// TODO Auto-generated constructor stub
		font = new BitmapFont(Gdx.files.internal("ui/font.fnt"), false);
		x = pause.getX() + pause.getWidth();

	}

	public void addScore(int Score) {
		this.Score += Score;
	}

	public void cleanScore() {
		this.Score = 0;
	}

	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		font.draw(batch, Score + "000", x, Gdx.graphics.getHeight() - 5);
	}
}
