package com.example.gamedemo;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class InfoScreen extends ScreenAdapter {

	Stage Ui;
	SpriteBatch Batch;
	private TextureAtlas atlas;
	private Sprite sprite;
	private Sprite Title;
	FirstGame Gm;
	Animation flyMove;
	Array<TextureRegion> keyFramesArray = new Array<TextureRegion>();
	float stateTime = 0f;
	float x, y;
	int regionWidth, regionHeight;
	AtlasRegion Region;
	float flyMovex;
	ImageButton Back;

	public InfoScreen(FirstGame tGm) {
		// TODO Auto-generated constructor stub
		this.Gm = tGm;

		Batch = new SpriteBatch();
		Ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		atlas = new TextureAtlas(Gdx.files.internal("shoot_background.pack"));
		sprite = atlas.createSprite("background");
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Title = atlas.createSprite("shoot_copyright");
		Region = atlas.findRegion("game_loading3");
		flyMovex = (Gdx.graphics.getWidth() - Region.getRegionWidth()) / 2;
		keyFramesArray.add(atlas.findRegion("game_loading1"));
		keyFramesArray.add(atlas.findRegion("game_loading2"));
		keyFramesArray.add(atlas.findRegion("game_loading3"));
		keyFramesArray.add(atlas.findRegion("game_loading4"));
		flyMove = new Animation(0.3f, keyFramesArray);
		x = (Gdx.graphics.getWidth() - Title.getWidth()) / 2;
		y = (Gdx.graphics.getHeight() - (Title.getHeight() + 150));
		regionWidth = Title.getRegionWidth();
		regionHeight = Title.getRegionHeight();
		Title.setPosition(x, y);

		ImageButtonStyle style = new ImageButtonStyle();
		style.imageUp = new TextureRegionDrawable(
				atlas.findRegion("btn_finish"));
		style.imageDown = new TextureRegionDrawable(
				atlas.findRegion("btn_finish"));
		Back = new ImageButton(style);

		Back.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.EndGame();

			}
		});
		Back.setX(Gdx.graphics.getWidth() - Back.getWidth() - 2);
		Back.setY(10);
		Ui.addActor(Back);
		Gdx.input.setInputProcessor(Ui);

		new Timer().scheduleTask(new Task() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("lxmlxm", "timer ok!");
				Gm.setScreen(new GameScreen(Gm));
			}
		}, 3);

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		Batch.begin();
		sprite.draw(Batch);
		Title.draw(Batch);
		stateTime += delta;
		TextureRegion frame = flyMove.getKeyFrame(stateTime, true);
		Batch.draw(frame, flyMovex, y - 80 - Region.getRegionHeight());
		Batch.end();
		Ui.draw();

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Gdx.input.setInputProcessor(Ui);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		Batch.dispose();
		Ui.dispose();
		atlas.dispose();

	}

}
