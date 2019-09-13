package com.wapbane.miwok;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		// set the content of the activity to use activity_main.xml
        setContentView(R.layout.activity_main);

		/*
		 * Opens NumbersActivity when Numbers on the app list is clicked
		 */
		TextView numbersView = (TextView) findViewById(R.id.numbers);
		numbersView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				Intent intent = new Intent(view.getContext(), NumbersActivity.class);
				startActivity(intent);
			}
			});
		/*
		 * Opens FamilyActivity when Numbers on the app list is clicked
		 */
		TextView familyView = (TextView) findViewById(R.id.family);
		familyView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					Intent intent = new Intent(view.getContext(), FamilyActivity.class);
					startActivity(intent);
				}
			});
		/*
		 * Opens ColorsActivity when Numbers on the app list is clicked
		 */
		TextView colorsView = (TextView) findViewById(R.id.colors);
		colorsView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					Intent intent = new Intent(view.getContext(), ColorsActivity.class);
					startActivity(intent);
				}
			});
		/*
		 * Opens PhrasesActivity when Numbers on the app list is clicked
		 */
		TextView phrasesView = (TextView) findViewById(R.id.phrases);
		phrasesView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					Intent intent = new Intent(view.getContext(), PhrasesActivity.class);
					startActivity(intent);
				}
			});
    }
}

