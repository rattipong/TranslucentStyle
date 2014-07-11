package com.woalk.apps.xposed.translucentstyle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	protected Spinner spinner_status;
	protected Spinner spinner_nav;
	
	protected View view_status;
	protected View view_nav;

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		spinner_status = (Spinner) findViewById(R.id.spinner1);
		spinner_nav = (Spinner) findViewById(R.id.spinner2);
		
		view_status = findViewById(R.id.view1);
		view_nav = findViewById(R.id.view2);

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		((TextView) findViewById(R.id.textView_info)).setText(getString(R.string.copyright_info) + "\n\n" + getString(R.string.prefix_version) + " " + pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		final SharedPreferences sPref = getApplicationContext().getSharedPreferences(Statics.PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
		
		spinner_status.setSelection(sPref.getInt(Statics.PREF_STATUSDRAWABLE, 0));
		spinner_nav.setSelection(sPref.getInt(Statics.PREF_NAVDRAWABLE, 0));
		
		spinner_status.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences.Editor edit = sPref.edit();
				edit.putInt(Statics.PREF_STATUSDRAWABLE, position);
				edit.apply();
				view_status.setBackgroundResource(Statics.getDrawable(position, false));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner_nav.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences.Editor edit = sPref.edit();
				edit.putInt(Statics.PREF_NAVDRAWABLE, position);
				edit.apply();
				view_nav.setBackgroundResource(Statics.getDrawable(position, true));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.title_restart);
				builder.setMessage(R.string.msg_restart);
				builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Process proc = Runtime.getRuntime()
									.exec(new String[]{ "su", "-c", "busybox killall com.android.systemui"});
							proc.waitFor();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				builder.setNegativeButton(android.R.string.no, null);
				builder.show();
			}
		});
	}
}