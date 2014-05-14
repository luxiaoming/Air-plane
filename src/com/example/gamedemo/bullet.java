package com.example.gamedemo;

import java.util.Random;

import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class bullet {
	Random r = new Random(1000);
	private Array<Rectangle> bulletArray = new Array<Rectangle>();
	private Sprite bullet1Texture;
	private Sprite bullet2Texture;
	private TextureAtlas atlas;
	private int bullet1W;
	private int bullet1H;
	private int bullet2W;
	private int bullet2H;
	private float stateTime = 0f;
	private int Type = TypeBullet1;
	public static final int TypeBullet2 = 1;
	public static final int TypeBullet1 = 0;
	private int hreoW;
	private float stateTimeforbullet2 = 0f;
	private float stateTimedf = 10f; // 持续5s的增强攻击

	// private boolean isTr
	public bullet(TextureAtlas atlas) {
		// TODO Auto-generated constructor stub
		this.atlas = atlas;

		bullet1Texture = new Sprite(atlas.findRegion("bullet1"));
		bullet2Texture = new Sprite(atlas.findRegion("bullet2"));
		bullet1W = bullet1Texture.getRegionWidth();
		bullet1H = bullet1Texture.getRegionHeight();
		bullet2W = bullet2Texture.getRegionWidth();
		bullet2H = bullet2Texture.getRegionHeight();
	}

	public void creatbullet(int x, int y, int W) {
		// 构造一个子弹，需要增加参数，判断是否为普通还是增强模式
		hreoW = W;
		if (Type == TypeBullet1) {
			Rectangle tmp = Pools.obtain(Rectangle.class);
			tmp.set(x - bullet1W / 2, y, bullet1W, bullet1H);
			bulletArray.add(tmp);
		} else {
			Rectangle tmp = Pools.obtain(Rectangle.class);
			tmp.set(x - bullet1W / 2 - W / 4, y, bullet1W, bullet1H);
			bulletArray.add(tmp);
			Rectangle tmp2 = Pools.obtain(Rectangle.class);
			tmp2.set(x - bullet1W / 2 + W / 4, y, bullet1W, bullet1H);
			bulletArray.add(tmp2);
		}

	}

	public void setType(int Type) {
		this.Type = Type;
		stateTimeforbullet2 = 0;
	}

	public void act(float delta, int x, int y) {
		// 依据时间戳，移动子弹
		stateTime += delta;
		stateTimeforbullet2 += delta;
		if (Type == TypeBullet2 && stateTimeforbullet2 > stateTimedf) {
			Type = TypeBullet1;
			stateTimeforbullet2 = 0f;
		}
		if (stateTime > time_df) {
			stateTime = 0;
			creatbullet(x, y, hreoW);
		}
		int index = 0;
		for (index = bulletArray.size - 1; index >= 0; index--) {

			bulletArray.get(index).y += pix_df * delta;
			if (bulletArray.get(index).y >= 854) {
				bulletArray.removeIndex(index);
			}
		}

	}

	public void draw(SpriteBatch batch) {
		// 刷新数据

		for (Rectangle bullet : bulletArray) {
			if (Type == TypeBullet1) {
				bullet1Texture.setX(bullet.getX());
				bullet1Texture.setY(bullet.getY());
				bullet1Texture.draw(batch);
			} else {
				bullet2Texture.setX(bullet.getX());
				bullet2Texture.setY(bullet.getY());
				bullet2Texture.draw(batch);
			}
		}
	}

	public Array<Rectangle> getbulletArray() {
		//
		return bulletArray;
	}

	static final float time_df = 0.16f;
	static final int pix_df = 1800;
}
