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

package com.yogeshn.fyp.androidclient.androidtextspeech.web;

public enum URLGoogleAPI {
	TRANSLATE_TEXT(
			"http://translate.google.com.br/translate_a/t?client=t&multires=1&prev=btn&ssel=0&tsel=0&sc=1&hl=%s&sl=%s&tl=%s"), TRANSLATE_AUDIO(
			"http://translate.google.com/translate_tts?q=%s&tl=%s"), TRANSLATE_DETECT(
			"http://www.google.com/uds/GlangDetect?v=1.0&q=%s");

	private final String url;

	private URLGoogleAPI(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
