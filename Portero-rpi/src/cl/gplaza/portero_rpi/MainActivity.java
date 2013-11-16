package cl.gplaza.portero_rpi;

import org.json.JSONObject;

import cl.gplaza.portero_rpi.RestClient.RequestMethod;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button buttonOpen;
	private static final int RESULT_SETTINGS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		addListenerOnButton();
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
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;

		}

		return true;
	}

	public void addListenerOnButton() {

		buttonOpen = (Button) findViewById(R.id.button1);
		buttonOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				openDoor();
			}
		});
	}

	private void openDoor() {

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String user = sharedPrefs.getString("prefUsername", "NULL");
		String password = sharedPrefs.getString("prefPassword", "NULL");
		String url = sharedPrefs.getString("prefUrl", "NULL");
		
		RestClient client = new RestClient(url);
		client.AddParam("user", user);
		client.AddParam("password", password);
		
		Boolean result = false;
		
		try {
			
			client.Execute(RequestMethod.POST);
			String response = client.getResponse();
			
			JSONObject jsonObject = new JSONObject(response);
			result = (Boolean) jsonObject.get("success");
			//Log.d("cl.gplaza.portero_rpi", result);
						
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextView settingsTextView = (TextView) findViewById(R.id.textView1);
		settingsTextView.setText("Resultado : " + (result? "Exito" : "Error"));
		
	}

}
