package com.wapbane.miwok;

import android.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class WordAdapter extends ArrayAdapter<Word> {
	// Resource ID for the background color for this list of words
	private int mColorRId;
	
	public WordAdapter(Activity context, ArrayList<Word> numbers, int colorRID){
		super(context, 0, numbers);
		mColorRId = colorRID;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		// Get the {@link Word} object located at this position in the list
		Word currentWord = getItem(position);
		
		// Check if the existing view is being reused, otherwise inflate the view
		View listItemView = convertView;
		if(listItemView == null){
			listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
		}
		
		// set the background color of the list view items
		View textGroup = listItemView.findViewById(R.id.list_item_content);
		// find the color that the resource ID maps to
		// int color = ContextCompat.getColor(getContext(), mColorRId);
		textGroup.setBackgroundResource(mColorRId);
		
		// Find the miwok number text
		TextView miwokNumber = (TextView) listItemView.findViewById(R.id.miwok_number);
		// get the miwok number from Word object and set this in the view
		miwokNumber.setText(currentWord.getMiwokTranslation());

		// Find the default number text
		TextView defaultNumber = (TextView) listItemView.findViewById(R.id.default_number);
		// get the default number from Word object and set this in the view
		defaultNumber.setText(currentWord.getDefaultTranslation());

		// Find the view image
		ImageView viewImage = (ImageView) listItemView.findViewById(R.id.view_image);
		
		if(currentWord.hasImage()){
			// get the view image from Word object and set this in the view
			viewImage.setImageResource(currentWord.getViewImage());
			viewImage.setMinimumWidth(96);
			
			// make sure the view image is visible
			viewImage.setVisibility(View.VISIBLE);
		} else {
			// hide the view image (set visibility to.GONE)
			viewImage.setVisibility(View.GONE);
		}
		
		return listItemView;
	}
}
