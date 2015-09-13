/*(The MIT License)

Copyright (c) 2013 Vinicius Oliveira - https://github.com/viniciusmo/android-text-to-speech

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files
(the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE.*/

package com.yogeshn.fyp.androidclient.androidtextspeech.translate;

import java.io.IOException;
import java.net.URLEncoder;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.yogeshn.fyp.androidclient.androidtextspeech.Language;
import com.yogeshn.fyp.androidclient.androidtextspeech.web.URLGoogleAPI;


@SuppressWarnings({"UnnecessaryLocalVariable", "CanBeFinal", "UnusedParameters"})
public class TranslatorSpeech implements Translatable {
	private String text;
	@SuppressWarnings("unused")
	private Language from;

	public TranslatorSpeech(String text, Language from) {
		this.text = text;
	}

	@SuppressWarnings("deprecation")
	public String getUrl() {
		String format = URLGoogleAPI.TRANSLATE_AUDIO.getUrl();
		String textEncoded = URLEncoder.encode(text);
		return String.format(format, textEncoded, "en-gb");
	}

	public void speack() {
		try {
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(getUrl());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			throw new TranslateException();
		}

	}
}
