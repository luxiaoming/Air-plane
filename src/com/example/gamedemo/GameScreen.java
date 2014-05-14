package com.example.gamedemo;

import java.util.Random;

import android.util.Log;

import cn.waps.AppConnect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen extends ScreenAdapter {
	Random r = new Random(1000);// 创建一个随机数
	Stage Ui;
	SpriteBatch Batch;
	private TextureAtlas atlas;
	private TextureAtlas shoot_background;
	private Hero hero;
	private Sprite background;
	FirstGame Gm;
	Animation flyMove;
	Array<TextureRegion> keyFramesArray = new Array<TextureRegion>();
	float stateTime = 0f;
	float stateTimeforEnemy1 = 0f;
	float stateTimeforEnemy2 = 0f;
	float stateTimeforEnemy3 = 0f;
	float stateTimeforUfo = 0f;
	float stateTimedfforEnemy1 = 0.5f;
	float stateTimedfforEnemy2 = 1.5f;
	float stateTimedfforEnemy3 = 5f;
	float x, y;
	int regionWidth, regionHeight;
	AtlasRegion Region;
	float flyMovex;
	ImageButton Back;
	private Music music;
	private Sound bulletSound;
	static public Sound Enemy1;
	static public Sound Enemy2;
	static public Sound Enemy3;
	float touchdownx, touchdowny;
	private bullet bulletGroup;
	private Enemy EnemyGroup;
	float df_y = 0;
	boolean isGameOver;
	GameScore Score;
	private BitmapFont font;
	private ImageButton pause;
	static public float Screen_W;
	static public float Screen_H;
	private Image Ufo1;
	private Image Ufo2;
	boolean isUfo1;
	private ImageButton baoxiang;
	private ImageButton bomb; // 炸弹
	private int bombnum;

	public FirstGame getGame() {

		return Gm;
	}

	// 初始化Ufo1
	private void initUfo1() {
		int x = r.nextInt((int) (Screen_W - Ufo1.getWidth()));

		Ufo1.setPosition(x, Screen_H * 4 / 5);
		MoveToAction move3 = Actions.moveTo(x, 0 - Ufo1.getHeight() - 10, 2f);
		move3.setInterpolation(Interpolation.circleOut);
		Ufo1.addAction(Actions.sequence(move3));
	}

	// 初始化Ufo2
	private void initUfo2() {
		int x = r.nextInt((int) (Screen_W - Ufo2.getWidth()));
		Ufo2.setPosition(x, Screen_H * 4 / 5);
		MoveToAction move3 = Actions.moveTo(x, 0 - Ufo2.getHeight() - 10, 2f);
		move3.setInterpolation(Interpolation.circleOut);
		Ufo2.addAction(Actions.sequence(move3));
	}

	public GameScreen(FirstGame Gme) {
		// TODO Auto-generated constructor stub
		this.Gm = Gme;
		Batch = new SpriteBatch();
		Screen_W = Gdx.graphics.getWidth();
		Screen_H = Gdx.graphics.getHeight();
		Ui = new UiStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// 场景构造
		font = new BitmapFont(Gdx.files.internal("ui/font.fnt"), false);
		shoot_background = new TextureAtlas(
				Gdx.files.internal("shoot_background.pack"));
		background = shoot_background.createSprite("background");
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		atlas = new TextureAtlas(Gdx.files.internal("baohe.pack"));
		baoxiang = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("tools_time_box")), new TextureRegionDrawable(
				atlas.findRegion("tools_time_box")));
		baoxiang.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.ShowOff();
				Gm.getScreen().pause();
			}
		});

		atlas = new TextureAtlas(Gdx.files.internal("shoot.pack"));
		Ufo1 = new Image(atlas.findRegion("ufo1"));
		Ufo1.setPosition(0, 0 - Ufo1.getHeight() - 20);
		Ufo2 = new Image(atlas.findRegion("ufo2")); // 设置到不可见的位置，后面使用
		Ufo2.setPosition(0, 0 - Ufo1.getHeight() - 20);
		pause = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("game_pause_nor")), new TextureRegionDrawable(
				atlas.findRegion("game_pause_pressed")));
		pause.setPosition(0,
				Gdx.graphics.getHeight()
						- atlas.findRegion("game_pause_nor").getRegionHeight());
		pause.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.setScreen(new PauseScreen(GameScreen.this, false));
			}
		});
		bomb = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("bomb")), new TextureRegionDrawable(
				atlas.findRegion("bomb")));
		bomb.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				bombnum = GetInt("Ufo2");
				Log.i("lxmlxm", bombnum + "");
				if (bombnum > 0) {
					PutInt("Ufo2", bombnum - 1);
					EnemyGroup.bomb(Score);// 炸掉所有敌机
				}
			}
		});
		baoxiang.setPosition(0, bomb.getHeight() + baoxiang.getWidth());
		baoxiang.getImage().setRotation(-90f);
		Score = new GameScore(pause);
		Array<TextureRegion> Texturelist = new Array<TextureRegion>();
		Array<TextureRegion> TextureOverlist = new Array<TextureRegion>();
		regionWidth = atlas.findRegion("hero1").getRegionWidth();
		regionHeight = atlas.findRegion("hero1").getRegionHeight();
		Texturelist.add(atlas.findRegion("hero1"));
		Texturelist.add(atlas.findRegion("hero2"));
		TextureOverlist.add(atlas.findRegion("hero_blowup_n1"));
		TextureOverlist.add(atlas.findRegion("hero_blowup_n2"));
		TextureOverlist.add(atlas.findRegion("hero_blowup_n3"));
		hero = new Hero(0.3f, Texturelist, TextureOverlist);// 创建主角
		x = (Gdx.graphics.getWidth() - regionWidth) / 2;
		y = 5;
		hero.setPosition(x, y);
		hero.setWidth(regionWidth);
		hero.setHeight(regionHeight);
		Log.i("lxm", "" + hero.getWidth() + " h=" + hero.getHeight());
		bulletGroup = new bullet(atlas); // 构造子弹
		EnemyGroup = new Enemy(atlas); // 构造敌人
		EnemyGroup.creatEnemy(Enemy.EnemyType1);
		Ui.addActor(baoxiang);
		Ui.addActor(pause);
		Ui.addActor(Ufo1);
		Ui.addActor(Ufo2);
		Ui.addListener(new InputListener() {
			/**
			 * Called when a mouse button or a finger touch goes down on the
			 * actor. If true is returned, this listener will receive all
			 * touchDragged and touchUp events, even those not over this actor,
			 * until touchUp is received. Also when true is returned, the event
			 * is {@link Event#handle() handled}.
			 * 
			 * @see InputEvent
			 */
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				Log.i("lxm touchDown", "x=" + x + " y=" + y);
				touchdownx = x;
				touchdowny = y;
				return true;
			}

			/**
			 * Called when a mouse button or a finger touch goes up anywhere,
			 * but only if touchDown previously returned true for the mouse
			 * button or touch. The touchUp event is always
			 * {@link Event#handle() handled}.
			 * 
			 * @see InputEvent
			 */
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
			}

			/**
			 * Called when a mouse button or a finger touch is moved anywhere,
			 * but only if touchDown previously returned true for the mouse
			 * button or touch. The touchDragged event is always
			 * {@link Event#handle() handled}.
			 * 
			 * @see InputEvent
			 */
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				// Log.i("lxm touchDragged ",
				// "x=" + x + " y=" + y + "X=" + hero.getX() + "Y="
				// + hero.getY() + "ParentX=" + Ui.getWidth()
				// + "ParentH=" + Ui.getHeight());
				//
				// Log.i("lxm touchDragged ",
				// "y=" +(Y + regionHeight));

				float X = hero.getX() + x - touchdownx;
				float Y = hero.getY() + y - touchdowny;
				touchdownx = x;
				touchdowny = y;
				// Log.i("lxm touchDragged ", "y=" + (Y + regionHeight) + "h="
				// + hero.getHeight() + "Pos=" + (854 - regionHeight - 2));
				X = X <= 0 ? 0 : (X + regionWidth) >= Screen_W ? Screen_W
						- regionWidth : X;
				Y = Y <= 0 ? 0 : (Y + regionHeight) >= Screen_H ? Screen_H
						- regionHeight - 2 : Y;
				Log.i("lxm", X + "--" + Y);
				hero.setPosition(X, Y);

			}
		});
		bulletGroup.creatbullet((int) (hero.getX() + regionWidth / 2),
				(int) (hero.getY() + regionHeight), regionWidth);
		Gdx.input.setInputProcessor(Ui);
		Gdx.input.setCatchBackKey(true);
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/game_music.mp3"));
		music.setLooping(true);
		music.play();
		bulletSound = Gdx.audio
				.newSound(Gdx.files.internal("sound/bullet.mp3"));// Gdx.audio.newSound(Gdx.files.getFileHandle("data/shotgun.ogg",
		Enemy1 = Gdx.audio
				.newSound(Gdx.files.internal("sound/enemy1_down.mp3"));
		Enemy2 = Gdx.audio
				.newSound(Gdx.files.internal("sound/enemy2_down.mp3"));
		Enemy3 = Gdx.audio
				.newSound(Gdx.files.internal("sound/enemy3_down.mp3"));
		waitForLoadCompleted(bulletSound); // FileType.Internal));//Gdx.audio.newSound(Gdx.files.internal("data/shotgun.ogg"));

		bulletSound.loop(1, 2f, 0);
		Ui.addActor(bomb);
		Ui.addActor(hero);
	}

	private long waitForLoadCompleted(Sound sound) {
		long id;
		while ((id = sound.play(0)) == -1) {
			long t = TimeUtils.nanoTime();
			while (TimeUtils.nanoTime() - t < 100000000)
				;
		}
		return id;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		stateTime += delta;

		stateTimeforEnemy1 += delta;
		stateTimeforEnemy2 += delta;
		stateTimeforEnemy3 += delta;
		stateTimeforUfo += delta;
		df_y--;
		if (stateTimeforEnemy3 > stateTimedfforEnemy3) {
			stateTimeforEnemy3 = 0;
			stateTimedfforEnemy3 = 10f + r.nextFloat() * 20f;
			EnemyGroup.creatEnemy(Enemy.EnemyType3);

		} else if (stateTimeforEnemy2 > stateTimedfforEnemy2) {
			stateTimeforEnemy2 = 0;
			stateTimedfforEnemy2 = 1.5f + r.nextFloat() * 3f;
			EnemyGroup.creatEnemy(Enemy.EnemyType2);

		} else if (stateTimeforEnemy1 > stateTimedfforEnemy1) {
			stateTimeforEnemy1 = 0;
			stateTimedfforEnemy1 = 0.25f + r.nextFloat() * 1f;
			EnemyGroup.creatEnemy(Enemy.EnemyType1);

		}
		// 28s 形成一个ufo
		if (stateTimeforUfo > 28f) {
			stateTimeforUfo = 0;
			if (isUfo1) {
				initUfo1();
				isUfo1 = false;
			} else {
				initUfo2();
				isUfo1 = true;
			}

		}
		Rectangle Ufo = new Rectangle(Ufo1.getX(), Ufo1.getY(),
				Ufo1.getWidth(), Ufo1.getHeight());
		if (Ufo.overlaps(hero.getrectangle())) {
			Ufo1.setPosition(0, 0 - Ufo1.getHeight() - 20);
			Ufo1.clearActions();
			bulletGroup.setType(bullet.TypeBullet2);
			// 开启增强模式了
		}
		Rectangle Ufot = new Rectangle(Ufo2.getX(), Ufo2.getY(),
				Ufo2.getWidth(), Ufo2.getHeight());
		if (Ufot.overlaps(hero.getrectangle())) {
			Log.i("Remove", "ufo2");
			Ufo2.setPosition(0, 0 - Ufo2.getHeight() - 20);
			Ufo2.clearActions();
			bombnum = GetInt("Ufo2") + 1;
			PutInt("Ufo2", bombnum);
			// 得到一个炸弹

		}
		if (!isGameOver) {
			// 在非结束状态下，判断是否over
			isGameOver = EnemyGroup.checkbulletAndPlay(
					bulletGroup.getbulletArray(), hero.getrectangle(), Score);
			if (isGameOver) {
				hero.setStatus(Hero.eIsOvering);
				Gdx.input.setInputProcessor(null);
			}
		}
		bulletGroup.act(delta, (int) (hero.getX() + regionWidth / 2),
				(int) (hero.getY() + regionHeight));
		EnemyGroup.act(delta);
		draw(delta);
		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (isBackPressed) {
			Gm.setScreen(new PauseScreen(this, false));
		} else if ((isGameOver && hero.isOver())) {

			Gm.setScreen(new PauseScreen(this, true));
		}
	}

	public void draw(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Batch.begin();
		Batch.disableBlending();
		background.setY(df_y % Gdx.graphics.getHeight());
		background.draw(Batch);
		background.setY(Gdx.graphics.getHeight() + df_y
				% Gdx.graphics.getHeight());
		background.draw(Batch);
		Batch.enableBlending();
		Batch.end();
		Ui.act();
		Ui.draw();
		Batch.begin();
		bulletGroup.draw(Batch);
		EnemyGroup.draw(Batch, stateTime);
		Score.draw(Batch);
		font.draw(Batch, bombnum + "", Ufo2.getWidth() + 10,
				Ufo2.getHeight() / 2);
		Batch.end();

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Gdx.input.setInputProcessor(Ui);
		bulletSound.loop();

		isGameOver = false;
		hero.setStatus(Hero.eIsRuning);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		bulletSound.pause();
		hero.setStatus(Hero.eIsPause);
		Gdx.input.setInputProcessor(null);
		Gm.setScreen(new PauseScreen(this, false));
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(Ui);
		bulletSound.loop();
		if (isGameOver) {
			x = (Gdx.graphics.getWidth() - regionWidth) / 2;
			y = 5;
			hero.setPosition(x, y);
			EnemyGroup.cleanall();
			Score.cleanScore();
		}
		isGameOver = false;
		hero.setStatus(Hero.eIsRuning);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		bulletSound.pause();
		hero.setStatus(Hero.eIsPause);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		Batch.dispose();
		Ui.dispose();
		atlas.dispose();
		music.dispose();

	}

	public void PutInt(String name, int value) {
		Preferences Ps = Gdx.app.getPreferences("cfg");
		Ps.putInteger(name, value);
		Ps.flush();
	}

	public int GetInt(String name) {
		Preferences Ps = Gdx.app.getPreferences("cfg");
		return (int) Ps.getInteger(name, 0);
	}
}
