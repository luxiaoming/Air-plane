package com.example.gamedemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import cn.waps.AppConnect;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainActivity extends AndroidApplication {
	Game Gm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AppConnect.getInstance("dca242344a805d9afae7b59c263148f6", "default",
				this);
		AppConnect.getInstance(this).initPopAd(this);
		Gm = new FirstGame(MainActivity.this);
		// setLogLevel(LOG_DEBUG);
		initialize(Gm, false);
		//AppConnect.getInstance(this).showOffers(this);
		mContext = this;
	}

	private static Handler handler = handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				AppConnect.getInstance(mContext).showOffers(mContext);
				break;
			}

		}
	};
	private static Context mContext;

	public static void showAdStatic(int adTag) {
		Message msg = handler.obtainMessage();
		msg.what = adTag; // 私有静态的整型变量，开发者请自行定义值
		handler.sendMessage(msg);
	}

}
