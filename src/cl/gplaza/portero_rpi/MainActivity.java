package cl.gplaza.portero_rpi;

import cl.gplaza.portero_rpi.RestClient.RequestMethod;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

public class MainActivity extends Activity {

	Button buttonOpen;

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

	public void addListenerOnButton() {

		buttonOpen = (Button) findViewById(R.id.button1);

		buttonOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				RestClient client = new RestClient("http://192.168.0.28:8080/hello/gilles");
				
				try {
				    client.Execute(RequestMethod.GET);
				} catch (Exception e) {
				    e.printStackTrace();
				}

				String response = client.getResponse();
				Log.d("cl.gplaza.portero_rpi", response);
			}
		});
	}

}
