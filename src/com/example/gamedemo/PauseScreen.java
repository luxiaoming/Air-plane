package com.example.gamedemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class PauseScreen implements Screen {

	private GameScreen gameScreen;
	private Stage stage;
	private MyImageButton back;
	private NinePatch NineTexture;
	private TextureAtlas atlas;
	private SpriteBatch Batch;
	private BitmapFont font;
	private MyImageButton Exit;

	public PauseScreen(GameScreen tgameScreen, boolean isover) {
		this.gameScreen = tgameScreen;
		stage = new Stage(480, 854, true);
		Batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("menu/Pause.pack"));
		font = new BitmapFont(Gdx.files.internal("ui/font1.fnt"), false);
		NineTexture = new NinePatch(
				atlas.findRegion("game_burst_aircraft_button_bule_small_normal"),
				16, 16, 5, 5);
		// 继续按钮
		back = new MyImageButton(new NinePatchDrawable(NineTexture));
		back.setPosition(80, 854 - 854 / 3);
		back.setSize(480 - 160, 50);
		if (isover) {
			back.setLabel(new Label("重新开始", new LabelStyle(font, null)));
		} else {
			back.setLabel(new Label("继续", new LabelStyle(font, null)));
		}
		back.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.getGame().getScreen().dispose();
				gameScreen.getGame().setScreen(gameScreen);
			}
		});
		Exit = new MyImageButton(new NinePatchDrawable(NineTexture));
		Exit.setPosition(80, back.getY() - back.getHeight() - 50);
		Exit.setSize(480 - 160, 50);
		Exit.setLabel(new Label("退出游戏", new LabelStyle(font, null)));
		Exit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.getGame().EndGame();
			}
		});
		stage.addActor(back);
		stage.addActor(Exit);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		gameScreen.draw(delta);
		stage.act(delta);
		stage.draw();
		Batch.begin();
		Batch.end();

		// if (back.isPressed()) {
		// gameScreen.dispose();
		// gameScreen.getGame().getScreen().dispose();
		// }
		// if (restart.isPressed()) {
		// gameScreen.getGame().getScreen().dispose();
		// gameScreen.getGame().setScreen(gameScreen);
		// }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
