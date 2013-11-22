package cl.gplaza.portero_rpi;

import java.util.Map;

import org.json.JSONObject;

import cl.gplaza.portero_rpi.RestClient.RequestMethod;
import android.os.AsyncTask;

public class RestAsynTask extends AsyncTask<RestQueryObject, Integer, JSONObject> {

	@Override
	protected JSONObject doInBackground(RestQueryObject... params) {

		RestQueryObject restQueryObject = params[0];
		RestClient client = new RestClient(restQueryObject.getUrl());

		for (Map.Entry<String, String> entry : restQueryObject.getParams().entrySet()) {
			client.AddParam(entry.getKey(), entry.getValue());
		}

		JSONObject jsonObject = null;

		try {

			client.Execute(RequestMethod.POST);
			String response = client.getResponse();
			jsonObject = new JSONObject(response);
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return jsonObject;
	}

}
