package com.wapbane.miwok;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */

public class Word{
	
	/** Default translation for the word */
	private String mDefaultTranslation;
	/** Miwok translation for the word */
	private String mMiwokTranslation;
	/** ImageView for the item */
	private int mViewImage = NO_IMAGE_PROVIDED;
	/** audio for the word */
	private int mMiwokAudio;
	
	//
	private static final int NO_IMAGE_PROVIDED = -1;
	
	// Constructor for lists without ImageView
	public Word(String defaultTranslation, String miwokTranslation, int miwokAudio){
		mDefaultTranslation = defaultTranslation;
		mMiwokTranslation = miwokTranslation;
		mMiwokAudio = miwokAudio;
	}
	
	// Constructor for lists with ImageView
	public Word(String defaultTranslation, String miwokTranslation, int viewImage, int miwokAudio){
		mDefaultTranslation = defaultTranslation;
		mMiwokTranslation = miwokTranslation;
		mViewImage = viewImage;
		mMiwokAudio = miwokAudio;
	}

	/** Get default translation for the word */
	public String getDefaultTranslation(){
		return mDefaultTranslation;
	}

	/** Get miwok translation for the word */
	public String getMiwokTranslation(){
		return mMiwokTranslation;
	}
	
	/** Get image for the word */
	public int getViewImage(){
		return mViewImage;
	}
	
	/**
	 * Returns whether or not there is an image for this word class
	 *
	 */
	 public boolean hasImage(){
		 return mViewImage != NO_IMAGE_PROVIDED;
	 }

	/** Get miwok translation for the word */
	public int getAudio(){
		return mMiwokAudio;
	}
}
