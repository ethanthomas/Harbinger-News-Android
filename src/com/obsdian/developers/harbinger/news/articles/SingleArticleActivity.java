package com.obsdian.developers.harbinger.news.articles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleArticleActivity extends Activity {

	// JSON node keys
	private static final String TAG_TITLE = "title";
	private static final String TAG_BODY = "body";
	private static final String TAG_AUTHOR = "author";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_contact);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String title = in.getStringExtra(TAG_TITLE);
		String author = in.getStringExtra(TAG_AUTHOR);
		String body = in.getStringExtra(TAG_BODY);
		
		body.replace("&nbsp;", ".");


		// Displaying all values on the screen
		TextView title1 = (TextView) findViewById(R.id.title);
		TextView author1 = (TextView) findViewById(R.id.author);
		TextView body1 = (TextView) findViewById(R.id.body);

		title1.setText(title);
		author1.setText(author);
		body1.setText(body);
	}
}
