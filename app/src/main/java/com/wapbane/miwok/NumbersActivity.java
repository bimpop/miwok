package com.wapbane.miwok;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class NumbersActivity extends Activity{
	// declare mediaplayer object
	private MediaPlayer mNumbersPlayer;

	/** Handles audio focus when playing a sound file */
	private AudioManager mAudioManager;

	private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = 
	new AudioManager.OnAudioFocusChangeListener(){
		@Override
		public void onAudioFocusChange(int focusChange){
			if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||

			   focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
				// Pause playback
				mNumbersPlayer.pause();
				mNumbersPlayer.seekTo(0);
			}else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
				// Resume playback
				mNumbersPlayer.start();
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
		super.onCreate(savedInstanceState);

		// set content view to activity_numbers.xml
		setContentView(R.layout.word_list);

		// Create and setup tge {@link AudioManager} to request audio focus
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Create array of english numbers
		ArrayList<Word> numbers = new ArrayList<Word>();
		numbers.add(new Word("one", "lutti", R.drawable.ic_launcher, R.raw.number_one));
		numbers.add(new Word("two", "otiiko", R.drawable.ic_launcher, R.raw.number_two));
		numbers.add(new Word("three", "tolookosu", R.drawable.ic_launcher, R.raw.number_three));
		numbers.add(new Word("four", "oyyisa", R.drawable.ic_launcher, R.raw.number_four));
		numbers.add(new Word("five", "massokka", R.drawable.ic_launcher, R.raw.number_five));
		numbers.add(new Word("six", "temmokka", R.drawable.ic_launcher, R.raw.number_six));
		numbers.add(new Word("seven", "kenekaku", R.drawable.ic_launcher, R.raw.number_seven));
		numbers.add(new Word("eight", "kawinta", R.drawable.ic_launcher, R.raw.number_eight));
		numbers.add(new Word("nine", "wo'e", R.drawable.ic_launcher, R.raw.number_nine));
		numbers.add(new Word("ten", "na'aacha", R.drawable.ic_launcher, R.raw.number_ten));

		WordAdapter adapter = new WordAdapter(this, numbers, R.color.category_numbers);
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
						// or Word currentWord = numbers.get(position);
						Word currentWord = (Word) parent.getItemAtPosition(position);
						// numbers player initialized
						mNumbersPlayer = MediaPlayer.create(NumbersActivity.this, currentWord.getAudio());
						mNumbersPlayer.start();
						mNumbersPlayer.setOnCompletionListener(mCompletionListener);
					}

				}
			});
	}

	/**
	 * Clean up the media player by releasing its resources.
	 */
	private void releaseMediaPlayer(){
		// If the media is not null then it may be currently playing a sound
		if(mNumbersPlayer != null){
			// Regardless of the current state of the media player, release its resources
			// because we no longer need it.
			mNumbersPlayer.release();

			// Set the media player to null. For our code, we've decided that
			// setting the media player to null is an easy way to tell that the media player
			// is not configured to play an audio file at the moment
			mNumbersPlayer = null;

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
