package cl.gplaza.portero_rpi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
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
		
		RestQueryObject restQuery = new RestQueryObject();
		restQuery.setUrl(url);
		restQuery.setParams(params);

		RestAsynTask restTask = new RestAsynTask();
		restTask.execute(restQuery);

		user.setText("");
		password.setText("");
	}

}
