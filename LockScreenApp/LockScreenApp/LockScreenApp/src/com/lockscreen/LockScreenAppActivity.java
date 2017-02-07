package com.lockscreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LockScreenAppActivity extends Activity {
	public word_struct w_struct;
	private Button finish_btn;
	private RadioGroup rgroup;
	private RadioButton rb1, rb2, rb3;
	private TextView tv1;
	public static myDB mdb;
	public static SQLiteDatabase sqlDB;
	private int mod_num = 0, home_flag = 0, ans_num, player_num = 4;
	private int rand_index1, rand_index2, rand_index3, click_num = 0;
	private int input_ok = 0, is_finish = 0;
	private static final String TYPEFACE_NAME = "NEXONFootballGothicL.otf";
	private Typeface typeface = null;
	final ArrayList<word_struct> items = new ArrayList<word_struct>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadtypeface();
		setContentView(R.layout.main);
		mdb = new myDB(this);
		// global_score.earlyInit();
		try {

			// custom_list.xml 을 가져와 XmlPullParser 에 넣는다.
			XmlPullParser wordlist = getResources().getXml(R.xml.word);
			w_struct = new word_struct();
			// 파싱한 이 END_DOCUMENT(종료 태그)가 나올때 까지 반복한다.
			while (wordlist.getEventType() != XmlPullParser.END_DOCUMENT) {
				// 태그의 첫번째 속성일 때,

				if (wordlist.getEventType() == XmlPullParser.START_TAG) {
					// 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
					if (wordlist.getName().equals("word")) {
						w_struct.word = wordlist.getAttributeValue(0);
					} else if (wordlist.getName().equals("mean1")) {
						w_struct.mean1 = wordlist.getAttributeValue(0);
					} else if (wordlist.getName().equals("mean2")) {
						w_struct.mean2 = wordlist.getAttributeValue(0);
					} else if (wordlist.getName().equals("mean3")) {
						w_struct.mean3 = wordlist.getAttributeValue(0);
						input_ok = 1;
					}
					if (input_ok == 1) {
						items.add(w_struct);
						w_struct = new word_struct();
						input_ok = 0;
					}
				}
				// 다음 태그로 이동
				wordlist.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (home_flag == 1) {
			onUserLeaveHint();
		}
		array_swap();
		rand();
		radio();
		click();
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		finish();
	}

	public void click() {
		finish_btn = (Button) findViewById(R.id.finish);
		finish_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				click_num++;
				// TODO Auto-generated method stub
				switch (rgroup.getCheckedRadioButtonId()) {
				case R.id.rb1:
					player_num = 0;
					break;
				case R.id.rb2:
					player_num = 1;
					break;
				case R.id.rb3:
					player_num = 2;
					break;
				default:
					break;
				}
				if (player_num == ans_num) {
					if (click_num == 1) {
						plus_good();
					} else if (click_num == 2) {
						plus_soso();
					} else if (click_num == 3) {
						plus_bad();
					}
					is_finish = 1;
					finish();
				}
				if (player_num == 4) {
					click_num--;
				}
				if (is_finish == 0 && click_num == 1) {
					finish_btn.setBackgroundResource(R.drawable.soso_p);
				} else if (is_finish == 0 && click_num >= 2) {
					finish_btn.setBackgroundResource(R.drawable.bad_p);
				}
			}
		});
	}

	public void radio() {
		rgroup = (RadioGroup) findViewById(R.id.rgroup);
		tv1 = (TextView) findViewById(R.id.text_id);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb3 = (RadioButton) findViewById(R.id.rb3);
		if (mod_num == 0) {
			rb1.setText("  " + items.get(rand_index1).mean1
					+ items.get(rand_index1).mean2
					+ items.get(rand_index1).mean3);
			rb1.setTypeface(typeface);
			rb2.setText("  " + items.get(rand_index2).mean1
					+ items.get(rand_index2).mean2
					+ items.get(rand_index2).mean3);
			rb2.setTypeface(typeface);
			rb3.setText("  " + items.get(rand_index3).mean1
					+ items.get(rand_index3).mean2
					+ items.get(rand_index3).mean3);
			rb3.setTypeface(typeface);
			switch (ans_num) {
			case 0:
				tv1.setText(items.get(rand_index1).word);
				break;
			case 1:
				tv1.setText(items.get(rand_index2).word);
				break;
			case 2:
				tv1.setText(items.get(rand_index3).word);
				break;
			}
		} else if (mod_num == 1) {
			rb1.setText("  " + items.get(rand_index1).word);
			rb1.setTypeface(typeface);
			rb2.setText("  " + items.get(rand_index2).word);
			rb2.setTypeface(typeface);
			rb3.setText("  " + items.get(rand_index3).word);
			rb3.setTypeface(typeface);
			switch (ans_num) {
			case 0:
				tv1.setText(items.get(rand_index1).mean1
						+ items.get(rand_index1).mean2
						+ items.get(rand_index1).mean3);
				break;
			case 1:
				tv1.setText(items.get(rand_index2).mean1
						+ items.get(rand_index2).mean2
						+ items.get(rand_index2).mean3);
				break;
			case 2:
				tv1.setText(items.get(rand_index3).mean1
						+ items.get(rand_index3).mean2
						+ items.get(rand_index3).mean3);
				break;
			}
		}
		home_flag = 1;
	}

	public void rand() {
		rand_index1 = ((int) (Math.random() * items.size()));
		rand_index2 = rand_index1;
		rand_index3 = rand_index1;
		while (rand_index2 == rand_index1) {
			rand_index2 = ((int) (Math.random() * items.size()));
		}
		while (rand_index3 == rand_index1 || rand_index3 == rand_index2) {
			rand_index3 = ((int) (Math.random() * items.size()));
		}
		ans_num = ((int) (Math.random() * items.size())) % 3;
		mod_num = ((int) (Math.random() * items.size())) % 2;
	}

	public void array_swap() {
		int index1;
		int index2;
		HashSet<Integer> indexs = new HashSet<Integer>();
		while (indexs.size() != items.size()) {
			do {
				index1 = ((int) (Math.random() * items.size()));
			} while (indexs.contains(index1));
			indexs.add(index1);
			do {
				do {
					index2 = ((int) (Math.random() * items.size()));
				} while (index1 == index2);
			} while (indexs.contains(index2));
			indexs.add(index2);
			Collections.swap(items, index1, index2);
			if (items.size() - indexs.size() == 1) {
				break;
			}
		}
	}

	private void loadtypeface() {
		if (typeface == null) {
			typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
		}
	}

	@Override
	public void setContentView(int viewId) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(this).inflate(viewId, null);
		ViewGroup group = (ViewGroup) view;
		int childCnt = group.getChildCount();
		for (int i = 0; i < childCnt; i++) {
			View v = group.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(typeface);
			}
		}
		super.setContentView(view);
	}

	public void plus_good() {
		sqlDB = mdb.getReadableDatabase();
		int mgood = 0;
		Cursor cursor;
		cursor = sqlDB.rawQuery("SELECT * FROM scoreTBL;", null);
		while (cursor.moveToNext()) {
			mgood = cursor.getInt(1);
		}
		mgood++;
		if (mgood >= 500) mgood = 500;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET good =" + mgood + ";");
		sqlDB.close();
	}

	public void plus_soso() {
		sqlDB = mdb.getReadableDatabase();
		int msoso = 0;
		Cursor cursor;
		cursor = sqlDB.rawQuery("SELECT * FROM scoreTBL;", null);
		while (cursor.moveToNext()) {
			msoso = cursor.getInt(2);
		}
		msoso++;
		if (msoso >= 750) msoso = 750;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET soso =" + msoso + ";");
		sqlDB.close();
	}

	public void plus_bad() {
		sqlDB = mdb.getReadableDatabase();
		int mbad = 0;
		Cursor cursor;
		cursor = sqlDB.rawQuery("SELECT * FROM scoreTBL;", null);
		while (cursor.moveToNext()) {
			mbad = cursor.getInt(3);
		}
		mbad++;
		if (mbad >= 1000) mbad = 1000;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET bad =" + mbad + ";");
		sqlDB.close();
	}

	public class myDB extends SQLiteOpenHelper {

		public myDB(Context context) {
			super(context, "score", null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE IF NOT EXISTS scoreTBL (ex char(10)PRIMARY KEY, good int, soso int, bad int);");
			db.execSQL("INSERT OR IGNORE INTO scoreTBL VALUES ('ex',0,0,0)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			onCreate(db);
		}
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {

			return true;
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
			return true;
		}
		return false;
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
			finish();
			return true;
		}
		return false;
	}

	public void onDestroy() {

		super.onDestroy();
	}

}
