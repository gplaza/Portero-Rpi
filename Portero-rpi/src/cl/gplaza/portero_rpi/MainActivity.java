package cl.gplaza.portero_rpi;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends Activity {

	private Button buttonOpen;
	private ProgressBar mProgressBar;
	private TextView textView;
	private static final int RESULT_SETTINGS = 1;
	private static final int RESULT_USER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		buttonOpen = (Button) findViewById(R.id.button1);
		textView = (TextView) findViewById(R.id.textView1);
		
		mProgressBar.setVisibility(View.INVISIBLE);
		textView.setVisibility(View.INVISIBLE);
		
		buttonOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openDoor();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_settings:
			Intent i = new Intent(this, ConnectSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.action_user:
			Intent j = new Intent(this, UserSettingActivity.class);
			startActivityForResult(j, RESULT_USER);
			break;
		}

		return true;
	}

	private void openDoor() {

		Log.d("cl.gplaza.portero_rpi", "open the door funcion");
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String user = sharedPrefs.getString("prefUsername", "NULL");
		String password = sharedPrefs.getString("prefPassword", "NULL");
		String url = sharedPrefs.getString("prefUrl", "NULL");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", user);
		params.put("password", password);
		
		RestQueryObject restQuery = new RestQueryObject();
		restQuery.setUrl(url + "/login");
		restQuery.setParams(params);
		
		RestAsynTask restTask = new RestAsynTaskLogin();
		JSONObject result = null;
		Boolean resultado = false;
		
		try {
			
			result = restTask.execute(restQuery).get();
			resultado = result.getBoolean("success");
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		textView.setText("Resultado : " + (resultado ? "Exito" : "Error"));
		textView.setVisibility(View.VISIBLE);
		
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(70);
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(10);
		textView.startAnimation(anim);
		
		textView.setVisibility(View.INVISIBLE);
		
	}

	private class RestAsynTaskLogin extends RestAsynTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
	}

}
