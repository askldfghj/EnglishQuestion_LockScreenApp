package com.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Config extends Activity {
	private Button onbtn, offbtn, btn1;
	private TextView tv1, tv2, tv3;
	public Context mContext = null;
	public static myDB mdb;
	public static SQLiteDatabase sqlDB;
	private static int mgood = 0;
	private static int msoso = 0;
	private static int mbad = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		onbtn = (Button) findViewById(R.id.onbtn);
		offbtn = (Button) findViewById(R.id.offbtn);
		btn1 = (Button) findViewById(R.id.btn1);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		mContext = this;
		mdb = new myDB(this);
		DBset();
		onbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MyService.class);
				startService(intent);
			}
		});

		offbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MyService.class);
				stopService(intent);
			}
		});
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sqlDB = mdb.getWritableDatabase();
				sqlDB.execSQL("DROP TABLE IF EXISTS scoreTBL");
				sqlDB.close();
			}
		});
		scoreset();
	}

	public void plus_good() {
		mgood++;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET good =" + mgood +";");
		sqlDB.close();
	}

	public int get_good() {
		return mgood;
	}

	public void plus_soso() {
		msoso++;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET soso =" + msoso +";");
		sqlDB.close();
	}

	public int get_soso() {
		return msoso;
	}

	public void plus_bad() {
		mbad++;
		sqlDB = mdb.getWritableDatabase();
		sqlDB.execSQL("UPDATE scoreTBL SET bad =" + mbad +";");
		sqlDB.close();
	}

	public int get_bad() {
		return mbad;
	}

	public void scoreset() {
		sqlDB = mdb.getReadableDatabase();
		Cursor cursor;
		cursor = sqlDB.rawQuery("SELECT * FROM scoreTBL;", null);
		while (cursor.moveToNext()) 
		{
			mgood = cursor.getInt(1);
			msoso = cursor.getInt(2);
			mbad = cursor.getInt(3);
		}
		tv1.setText(Integer.toString(mgood));
		tv2.setText(Integer.toString(msoso));
		tv3.setText(Integer.toString(mbad));
		
	}
	
	public void DBset()
	{
		sqlDB = mdb.getWritableDatabase();
		mdb.onUpgrade(sqlDB, 1, 2);
		sqlDB.close();
	}

	public void earlyInit()
	{
		mdb = new myDB(this);
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
}
