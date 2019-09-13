package com.wapbane.miwok;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class FamilyActivity extends Activity{
	// declare family media player object
	private MediaPlayer mFamilyPlayer;

	/** Handles audio focus when playing a sound file */
	private AudioManager mAudioManager;
	
	private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = 
	new AudioManager.OnAudioFocusChangeListener(){
		@Override
		public void onAudioFocusChange(int focusChange){
			if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||

			   focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
				// Pause playback
				mFamilyPlayer.pause();
				mFamilyPlayer.seekTo(0);
			}else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
				// Resume playback
				mFamilyPlayer.start();
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
		ArrayList<Word> family = new ArrayList<Word>();
		family.add(new Word("father", "epe", R.drawable.ic_launcher, R.raw.family_father));
		family.add(new Word("mother", "eta", R.drawable.ic_launcher, R.raw.family_mother));
		family.add(new Word("son", "angsi", R.drawable.ic_launcher, R.raw.family_son));
		family.add(new Word("daughter", "tune", R.drawable.ic_launcher, R.raw.family_daughter));
		family.add(new Word("older brother", "taachi", R.drawable.ic_launcher, R.raw.family_older_brother));
		family.add(new Word("younger brother", "chalitti", R.drawable.ic_launcher, R.raw.family_younger_brother));
		family.add(new Word("older sister", "tete", R.drawable.ic_launcher, R.raw.family_older_sister));
		family.add(new Word("younger sister", "kolliti", R.drawable.ic_launcher, R.raw.family_younger_sister));
		family.add(new Word("grandmother", "ama", R.drawable.ic_launcher, R.raw.family_grandmother));
		family.add(new Word("grandfather", "paapa", R.drawable.ic_launcher, R.raw.family_grandfather));

		WordAdapter adapter = new WordAdapter(this, family, R.color.category_family);
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
					// get current family
					// or Word currentWord = family.get(position);
					Word currentWord = (Word) parent.getItemAtPosition(position);
					// family player initialized
					mFamilyPlayer = MediaPlayer.create(FamilyActivity.this, currentWord.getAudio());
					mFamilyPlayer.start();
					mFamilyPlayer.setOnCompletionListener(mCompletionListener);
				}
			}
		});
	}
	
	/**
	 * Clean uo tgmhe media player by releasing its resources.
	 */
	private void releaseMediaPlayer(){
		// If the media is not null then it may be currently playing a sound
		if(mFamilyPlayer != null){
			// Regardless of the current state of the media player, release its resources
			// because we no longer need it.
			mFamilyPlayer.release();

			// Set the media player to null. For our code, we've decided that
			// setting the media player to null is an easy way to tell that the media player
			// is not configured to play an audio file at the moment
			mFamilyPlayer = null;

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
