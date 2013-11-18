package cl.gplaza.portero_rpi;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import cl.gplaza.portero_rpi.RestClient.RequestMethod;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class UserSettingActivity extends Activity {

	private AtomPayListAdapter adapter;
	private EditText user;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);

		user = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);

		setupListViewAdapter();

		setupAddPaymentButton();
	}

	public void removeAtomPayOnClickHandler(View v) {
		AtomPayment itemToRemove = (AtomPayment) v.getTag();
		adapter.remove(itemToRemove);
	}

	private void setupListViewAdapter() {
		adapter = new AtomPayListAdapter(UserSettingActivity.this, R.layout.atom_pay_list_item, new ArrayList<AtomPayment>());
		ListView atomPaysListView = (ListView) findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
	}

	private void setupAddPaymentButton() {
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddItem();
			}
		});
	}

	private void AddItem() {
		String newUser = user.getText().toString();
		String newPassword = password.getText().toString();

		adapter.insert(new AtomPayment(newUser, newPassword), 0);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String url = sharedPrefs.getString("prefUrl", "NULL");
		String settingUser = sharedPrefs.getString("prefUsername", "NULL");
		String settingPassword = sharedPrefs.getString("prefPassword", "NULL");

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("user", settingUser);
		params.put("password", settingPassword);
		params.put("newu", newUser);
		params.put("newp", newPassword);
		params.put("url", url + "/register");

		RestAsynTask restTask = new RestAsynTask();
		restTask.execute(params);

		user.setText("");
		password.setText("");
	}

	private class RestAsynTask extends AsyncTask<HashMap<String, String>, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
		}

		@Override
		protected Boolean doInBackground(HashMap<String, String>... arg0) {

			HashMap<String, String> params = arg0[0];

			RestClient client = new RestClient(params.get("url"));
			client.AddParam("user", params.get("user"));
			client.AddParam("password", params.get("password"));
			client.AddParam("newu", params.get("newu"));
			client.AddParam("newp", params.get("newp"));

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

	}

}
