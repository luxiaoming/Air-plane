package com.example.gamedemo;

import java.util.Random;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class Enemy {
	Random r = new Random(1000);// 创建一个随机数
	private Array<EnemyParameter> EnemyArray = new Array<EnemyParameter>();
	private float stateTime = 0f;
	private float stateTimeforDraw = 0f;
	private AtlasRegion Enemy1Texture;
	float Enemy1Width;
	float Enemy1Height;
	private Animation Enemy1Anima;
	private AtlasRegion Enemy2Texture;
	private AtlasRegion Enemy2HitTexture;
	float Enemy2Width;
	float Enemy2Height;
	private Animation Enemy2Anima;
	private Animation Enemy3TextureAnima;
	float Enemy3Width;
	float Enemy3Height;
	private Animation Enemy3Anima;
	private AtlasRegion Enemy3HitTexture;

	// 敌人类别 ，0--3 从弱到强
	static final int EnemyType1 = 0;
	static final int EnemyType2 = 1;
	static final int EnemyType3 = 2;
	static final int EnemyType4 = 3;

	// AttackValue 当前无效，敌人当前不发子弹。
	public Enemy(TextureAtlas atlas) {
		// TODO Auto-generated constructor stub

		Enemy1Texture = atlas.findRegion("enemy1");
		Enemy1Width = Enemy1Texture.getRegionWidth();
		Enemy1Height = Enemy1Texture.getRegionHeight();
		Array<TextureRegion> Texturelist = new Array<TextureRegion>();
		Texturelist.add(atlas.findRegion("enemy1_down1"));
		Texturelist.add(atlas.findRegion("enemy1_down2"));
		Texturelist.add(atlas.findRegion("enemy1_down3"));
		Texturelist.add(atlas.findRegion("enemy1_down4"));
		Enemy1Anima = new Animation(0.16f, Texturelist);
		Texturelist.clear();
		Enemy2HitTexture = atlas.findRegion("enemy2_hit");
		Enemy2Texture = atlas.findRegion("enemy2");
		Enemy2Width = Enemy2Texture.getRegionWidth();
		Enemy2Height = Enemy2Texture.getRegionHeight();
		Texturelist.add(atlas.findRegion("enemy2_down1"));
		Texturelist.add(atlas.findRegion("enemy2_down2"));
		Texturelist.add(atlas.findRegion("enemy2_down3"));
		Texturelist.add(atlas.findRegion("enemy2_down4"));
		Enemy2Anima = new Animation(0.16f, Texturelist);
		Texturelist.clear();
		Enemy3HitTexture = atlas.findRegion("enemy3_hit");
		AtlasRegion Enemy3Texture = atlas.findRegion("enemy3_n1");
		Enemy3Width = Enemy3Texture.getRegionWidth();
		Enemy3Height = Enemy3Texture.getRegionHeight();
		Texturelist.add(atlas.findRegion("enemy3_n1"));
		Texturelist.add(atlas.findRegion("enemy3_n2"));
		Enemy3TextureAnima = new Animation(1.2f, Texturelist, Animation.LOOP) {
		};
		Texturelist.clear();
		Texturelist.add(atlas.findRegion("enemy3_down1"));
		Texturelist.add(atlas.findRegion("enemy2_down2"));
		Texturelist.add(atlas.findRegion("enemy3_down3"));
		Texturelist.add(atlas.findRegion("enemy3_down4"));
		Texturelist.add(atlas.findRegion("enemy3_down5"));
		Texturelist.add(atlas.findRegion("enemy3_down6"));
		Enemy3Anima = new Animation(0.16f, Texturelist);
	}

	// 构造一个敌人，需要增加参数，判断是否为普通还是增强模式
	public void creatEnemy(int Type) {

		switch (Type) {

		case EnemyType1:
			EnemyParameter tmp = Pools.obtain(EnemyParameter.class);
			tmp.set(r.nextInt((int) (480 - Enemy1Width)), 854,
					(int) Enemy1Width, (int) Enemy1Height, EnemyType1, 5, 1);
			EnemyArray.add(tmp);
			break;
		case EnemyType2:
			EnemyParameter tmp2 = Pools.obtain(EnemyParameter.class);
			tmp2.set(r.nextInt((int) (480 - Enemy2Width)), 854,
					(int) Enemy2Width, (int) Enemy2Height, EnemyType2, 5, 4);
			EnemyArray.add(tmp2);
			break;
		case EnemyType3:
			EnemyParameter tmp3 = Pools.obtain(EnemyParameter.class);
			tmp3.set(r.nextInt((int) (480 - Enemy3Width)), 854,
					(int) Enemy3Width, (int) Enemy3Height, EnemyType3, 5, 10);
			EnemyArray.add(tmp3);
			break;
		default:
			break;
		}

	}

	public void act(float delta) {
		// 依据时间戳，移动敌人

		// stateTime += delta;
		// if (stateTime > time_df) {
		// Log.i("Enemy draw", "" + delta);
		// float Rate = stateTime / time_df;
		stateTime = 0;
		int index = 0;
		EnemyParameter Tmp;
		for (index = EnemyArray.size - 1; index >= 0; index--) {
			Tmp = EnemyArray.get(index);
			Tmp.df = (System.nanoTime() - Tmp.OverTime) / 1000000000.0f;
			if (Tmp.rectangle.y + Tmp.rectangle.height <= 0) {
				EnemyArray.removeIndex(index);
				continue;
			}
			switch (Tmp.Type) {
			case EnemyType1:
				if (Tmp.isover) {
					if (Enemy1Anima.isAnimationFinished(Tmp.df)) {
						EnemyArray.removeIndex(index);
					}
					continue;
				} else {
					Tmp.rectangle.y -= pix_df_type1 * delta;
					// Log.i("Enemy draw", "" + delta+"--"+Tmp.rectangle.y);
				}
				break;
			case EnemyType2:
				if (Tmp.isover) {
					if (Enemy2Anima.isAnimationFinished(Tmp.df)) {
						EnemyArray.removeIndex(index);
					}
					continue;
				} else {
					Tmp.rectangle.y -= pix_df_type2 * delta;
				}
				break;
			case EnemyType3:
				if (Tmp.isover) {
					if (Enemy3Anima.isAnimationFinished(Tmp.df)) {
						EnemyArray.removeIndex(index);
					}
					continue;
				} else {
					Tmp.rectangle.y -= pix_df_type3 * delta;
				}
				break;
			default:
				break;
			}

		}

	}

	public void draw(SpriteBatch batch, float delta) {
		// 刷新数据
		stateTimeforDraw += delta;
		for (EnemyParameter Enemy : EnemyArray) {
			switch (Enemy.Type) {
			case EnemyType1:

				if (Enemy.isover) {
					batch.draw(Enemy1Anima.getKeyFrame(Enemy.df),
							Enemy.rectangle.x, Enemy.rectangle.y);
				} else {
					batch.draw(Enemy1Texture, Enemy.rectangle.x,
							Enemy.rectangle.y);
				}
				break;
			case EnemyType2:
				if (Enemy.isover) {
					batch.draw(Enemy2Anima.getKeyFrame(Enemy.df),
							Enemy.rectangle.x, Enemy.rectangle.y);
				} else if (Enemy.isHit) {
					batch.draw(Enemy2HitTexture, Enemy.rectangle.x,
							Enemy.rectangle.y);
					Enemy.isHit = false;

				} else {
					batch.draw(Enemy2Texture, Enemy.rectangle.x,
							Enemy.rectangle.y);
				}
				break;
			case EnemyType3:
				if (Enemy.isover) {
					batch.draw(Enemy3Anima.getKeyFrame(Enemy.df),
							Enemy.rectangle.x, Enemy.rectangle.y);
				} else if (Enemy.isHit) {
					batch.draw(Enemy3HitTexture, Enemy.rectangle.x,
							Enemy.rectangle.y);
					Enemy.isHit = false;

				} else {
					batch.draw(
							Enemy3TextureAnima.getKeyFrame(stateTimeforDraw),
							Enemy.rectangle.x, Enemy.rectangle.y);
				}
				break;
			default:
				break;
			}

		}
	}

	public void bomb(GameScore Gs) {
		for (int index2 = EnemyArray.size - 1; index2 >= 0; index2--) {
			EnemyParameter Enemy = EnemyArray.get(index2);
			Enemy.isover = true;
			Enemy.OverTime = System.nanoTime();
			Enemy.Health = 0;
		}
	}

	public void cleanall() {
		if (EnemyArray != null) {
			EnemyArray.clear();
		}
	}

	//
	public boolean checkbulletAndPlay(Array<Rectangle> bulletArray,
			Rectangle Play, GameScore Gs) {
		// 检测 是否打中,返回是否游戏结束
		for (int index = bulletArray.size - 1; index >= 0; index--) {
			Rectangle Re = bulletArray.get(index);

			for (int index2 = EnemyArray.size - 1; index2 >= 0; index2--) {
				EnemyParameter Enemy = EnemyArray.get(index2);
				if (Enemy.rectangle.overlaps(Play)) {
					return true;
				}
				if ((!Enemy.isover) && Enemy.rectangle.overlaps(Re)) {

					Enemy.Health -= 1;
					if (Enemy.Health <= 0) {
						Enemy.isover = true;
						Enemy.OverTime = System.nanoTime();

						switch (Enemy.Type) {
						case EnemyType1:
							GameScreen.Enemy1.play();
							Gs.addScore(1);
							break;
						case EnemyType2:
							GameScreen.Enemy2.play();
							Gs.addScore(5);
							break;
						case EnemyType3:
							GameScreen.Enemy3.play();
							Gs.addScore(10);
							break;
						default:
							break;
						}
					} else {
						Enemy.isHit = true;
					}
					bulletArray.removeIndex(index);
					break;
				}
			}
		}
		return false;
	}

	static final int pix_df_type1 = 250;
	static final int pix_df_type2 = 200;
	static final int pix_df_type3 = 100;

}
