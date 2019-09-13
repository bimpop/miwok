package com.wapbane.miwok;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class PhrasesActivity extends Activity{
	// declare phrases media player object
	private MediaPlayer mPhrasesPlayer;

	/** Handles audio focus when playing a sound file */
	private AudioManager mAudioManager;
	
	private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = 
	new AudioManager.OnAudioFocusChangeListener(){
		@Override
		public void onAudioFocusChange(int focusChange){
			if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||

			   focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
				// Pause playback
				mPhrasesPlayer.pause();
				mPhrasesPlayer.seekTo(0);
			}else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
				// Resume playback
				mPhrasesPlayer.start();
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
		ArrayList<Word> phrases = new ArrayList<Word>();
		phrases.add(new Word("Where are you going?", "minto wuksus", R.raw.phrases_where_going));
		phrases.add(new Word("What is your name?", "tinna oyaase'ne", R.raw.phrases_what_name));
		phrases.add(new Word("My name is...", "oyaaset...", R.raw.phrases_name_is));
		phrases.add(new Word("How are you feeling?", "michekes?", R.raw.phrases_how_feeling));
		phrases.add(new Word("I'm feeling good", "kuchi achit", R.raw.phrases_feeling_good));
		phrases.add(new Word("Are you coming?", "eenes'aa", R.raw.phrases_are_coming));
		phrases.add(new Word("Yes, I'm coming", "hee' eenem", R.raw.phrases_yes_coming));
		phrases.add(new Word("I'm coming", "eenem", R.raw.phrases_coming));
		phrases.add(new Word("Let's go.", "yoowutis", R.raw.phrases_let_go));
		phrases.add(new Word("Come here.", "enni'nem", R.raw.phrases_come_here));

		WordAdapter adapter = new WordAdapter(this, phrases, R.color.category_phrases);
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
					// get current phrases
					// or Word currentWord = phrases.get(position);
					Word currentWord = (Word) parent.getItemAtPosition(position);
					// phrases player initialized
					mPhrasesPlayer = MediaPlayer.create(PhrasesActivity.this, currentWord.getAudio());
					mPhrasesPlayer.start();
					mPhrasesPlayer.setOnCompletionListener(mCompletionListener);
				}
			}
		});
	}

	/**
	 * Clean uo tgmhe media player by releasing its resources.
	 */
	private void releaseMediaPlayer(){
		// If the media is not null then it may be currently playing a sound
		if(mPhrasesPlayer != null){
			// Regardless of the current state of the media player, release its resources
			// because we no longer need it.
			mPhrasesPlayer.release();

			// Set the media player to null. For our code, we've decided that
			// setting the media player to null is an easy way to tell that the media player
			// is not configured to play an audio file at the moment
			mPhrasesPlayer = null;

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
