package com.wapbane.miwok;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class ColorsActivity extends Activity
{
	// declare color media player object
	private MediaPlayer mColorsPlayer;

	/** Handles audio focus when playing a sound file */
	private AudioManager mAudioManager;
	
	private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = 
	new AudioManager.OnAudioFocusChangeListener(){
		@Override
		public void onAudioFocusChange(int focusChange){
			if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||

			   focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
				// Pause playback
				mColorsPlayer.pause();
				mColorsPlayer.seekTo(0);
			}else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
				// Resume playback
				mColorsPlayer.start();
			}else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
				// The AUDIOFOCUS_LOSS case means we've lost audio focus and
				// stop playback and cleanup resources
				releaseMediaPlayer();
			}
		}
	};
	
	/**
	 * This listener gets triggered when the {@link MediaPlayer} has completed
	 * playing the audio file
	 */
	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){
		@Override
		public void onCompletion(MediaPlayer mp){
			releaseMediaPlayer();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);

		// set content view to activity_numbers.xml
		setContentView(R.layout.word_list);

		// Create and setup tge {@link AudioManager} to request audio focus
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Create array of english numbers
		ArrayList<Word> colors = new ArrayList<Word>();
		colors.add(new Word("red","wetetti",R.color.red,R.raw.color_red));
		colors.add(new Word("mustard yellow","chiwiite",R.color.mustard_yellow,R.raw.color_mustard_yellow));
		colors.add(new Word("dusty yellow","topiisa",R.color.dusty_yellow,R.raw.color_dusty_yellow));
		colors.add(new Word("green","chokokki",R.color.green,R.raw.color_green));
		colors.add(new Word("brown","takaakki",R.color.brown,R.raw.color_brown));
		colors.add(new Word("gray","topoppi",R.color.gray,R.raw.color_gray));
		colors.add(new Word("black","kululli",R.color.black,R.raw.color_black));
		colors.add(new Word("white","kelelli",R.color.white,R.raw.color_white));

		WordAdapter adapter = new WordAdapter(this, colors, R.color.category_colors);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		
		// handle click event
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				// release media player resources first
				releaseMediaPlayer();

				//request audio focus for playback
				int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
															 // Use the music stream.
															 AudioManager.STREAM_MUSIC,
															 // Request transient focus
															 AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				if(result == mAudioManager.AUDIOFOCUS_REQUEST_GRANTED){
					// We have audio focus now

					// Create and setup the{@link MediaPlayer} for the audio resource associated
					// with the current word
					// get current color
					// or Word currentWord = colors.get(position);
					Word currentWord = (Word) parent.getItemAtPosition(position);
					// colors player initialized
					mColorsPlayer = MediaPlayer.create(ColorsActivity.this, currentWord.getAudio());
					mColorsPlayer.start();
					mColorsPlayer.setOnCompletionListener(mCompletionListener);
				}
			}
		});
	}
	
	/**
	 * Clean uo tgmhe media player by releasing its resources.
	 */
	private void releaseMediaPlayer(){
		// If the media is not null then it may be currently playing a sound
		if(mColorsPlayer != null){
			// Regardless of the current state of the media player, release its resources
			// because we no longer need it.
			mColorsPlayer.release();

			// Set the media player to null. For our code, we've decided that
			// setting the media player to null is an easy way to tell that the media player
			// is not configured to play an audio file at the moment
			mColorsPlayer = null;

			// Abandon audio focus when playback is complete
			mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
		}
	}

	@Override
	protected void onStop(){
		super.onStop();
		releaseMediaPlayer();
	}

}
