package com.obsdian.developers.harbinger.news.articles;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get articles JSON
	private static String url = "http://www.harbingernews.net/articles.json";

	// JSON Node names
	private static final String TAG_ARTICLES = "articles";
	private static final String TAG_TITLE = "title";
	private static final String TAG_BODY = "body";
	private static final String TAG_AUTHOR = "author";

	JSONArray articles = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> List;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String title = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				String body = ((TextView) view.findViewById(R.id.body))
						.getText().toString();
				String author = ((TextView) view.findViewById(R.id.author))
						.getText().toString();

				// Starting single articleMap activity
				Intent in = new Intent(getApplicationContext(),
						SingleArticleActivity.class);
				in.putExtra(TAG_TITLE, title);
				in.putExtra(TAG_BODY, body);
				in.putExtra(TAG_AUTHOR, author);

				startActivity(in);
				

				
			}
		});

		// Calling async task to get json
		new GetArticles().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetArticles extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					articles = jsonObj.getJSONArray(TAG_ARTICLES);

					// looping through All articles
					for (int i = 0; i < articles.length(); i++) {
						JSONObject a = articles.getJSONObject(i);

						String title = a.getString(TAG_TITLE);
						String body = a.getString(TAG_BODY);
						String author = a.getString(TAG_AUTHOR);

						// Phone node is JSON Object


						// tmp hashmap for single articleMap
						HashMap<String, String> articleMap = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						articleMap.put(TAG_TITLE, title);
						articleMap.put(TAG_BODY, body);
						articleMap.put(TAG_AUTHOR, author);

						// adding articleMap to articleMap list
						
						List.add(articleMap);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(MainActivity.this,
					List, R.layout.list_item, new String[] { TAG_TITLE,
							TAG_BODY, TAG_AUTHOR }, new int[] { R.id.title,
							R.id.body, R.id.author });

			setListAdapter(adapter);
		}

	}

}