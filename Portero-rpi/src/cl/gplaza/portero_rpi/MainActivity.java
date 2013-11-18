package cl.gplaza.portero_rpi;

import org.json.JSONObject;

import cl.gplaza.portero_rpi.RestClient.RequestMethod;
import android.os.AsyncTask;
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

		Access person = new Access();

		person.setUser(user);
		person.setUrl(url + "/login");
		person.setPassword(password);

		RestAsynTask restTask = new RestAsynTask();
		restTask.execute(person);
		
	}

	private class RestAsynTask extends AsyncTask<Access, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(Access... access) {
			
			Access person = access[0];

			String user = person.getUser();
			String password = person.getPassword();
			String url = person.getUrl();

			RestClient client = new RestClient(url);
			client.AddParam("user", user);
			client.AddParam("password", password);

			Boolean result = false;
						
			try {

				client.Execute(RequestMethod.POST);
				String response = client.getResponse();

				JSONObject jsonObject = new JSONObject(response);
				result = (Boolean) jsonObject.get("success");
	
			} catch (Exception e) {

				e.printStackTrace();
				return false;
			}

			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			mProgressBar.setVisibility(View.INVISIBLE);
			
			textView.setText("Resultado : " + (result ? "Exito" : "Error"));
			textView.setVisibility(View.VISIBLE);
			
			Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(70);
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(10);
			textView.startAnimation(anim);
			
			textView.setVisibility(View.INVISIBLE);
		}

	}

}
