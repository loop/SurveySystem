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

package com.yogeshn.fyp.androidclient.androidtextspeech;

import android.content.Context;

import com.yogeshn.fyp.androidclient.androidtextspeech.translate.OnCompleteLoad;
import com.yogeshn.fyp.androidclient.androidtextspeech.translate.TranslatorSpeech;


@SuppressWarnings({"SameParameterValue", "UnusedParameters"})
public class Speaker {

	public static void speak(final String text, final Language from,
			final Context context) {
		TranslatorSpeech translatorSpeech = new TranslatorSpeech(text, from);
		translatorSpeech.speack();
	}

	public static void speak(final String text, final Language from,
			final Context context, final OnCompleteLoad onCompleteLoad) {
		new Thread() {
			public void run() {
				TranslatorSpeech translatorSpeech = new TranslatorSpeech(text,
						from);
				translatorSpeech.speack();
				onCompleteLoad.onCompleteLoaded();
			}
		}.start();
	}
}
