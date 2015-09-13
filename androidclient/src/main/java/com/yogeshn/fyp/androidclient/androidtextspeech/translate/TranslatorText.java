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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;


import com.yogeshn.fyp.androidclient.androidtextspeech.Language;
import com.yogeshn.fyp.androidclient.androidtextspeech.web.URLGoogleAPI;
import com.yogeshn.fyp.androidclient.androidtextspeech.web.WebClient;

@SuppressWarnings("UnusedParameters")
public class TranslatorText implements Translatable {

	private Language from = Language.ENGLISH;
	private Language to = Language.ENGLISH;
	private final String textForTranslate;
	private StringBuilder textTranslated;

	public TranslatorText(Language from, Language to, String textForTranslate) {
		this.from = from;
		this.to = to;
		this.textForTranslate = verifyTextIsEmpty(textForTranslate);
	}

    private String verifyTextIsEmpty(String text) {
		if (text.isEmpty()) {
			throw new IllegalArgumentException("TextForTranslate is empty");
		}
		return text;
	}

	private void doParse(String content) {
		try {
			int index = 0;
			getTextTranslated(content, index);
		} catch (JSONException e) {
			throw new TranslateException();
		}
	}

	private void getTextTranslated(String content, int index)
			throws JSONException {
		JSONArray jsonArray = new JSONArray(content);
		JSONArray sentences = jsonArray.getJSONArray(0);
		for (int i = 0; i < sentences.length(); i++) {
			JSONArray sentence = sentences.getJSONArray(i);
			textTranslated.append(sentence.get(0).toString());
		}
	}

	public String getUrl() {
		String url = null;
		url = formatURL();
		return url;
	}

	private String formatURL() {
		String url;
		String format = URLGoogleAPI.TRANSLATE_TEXT.getUrl();
		url = String.format(format, from.getPrefix(), from.getPrefix(),
				to.getPrefix());
		return url;
	}

	public String translateText() {
		textTranslated = new StringBuilder();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("q", textForTranslate));
		String content = WebClient.doPost(getUrl(), nameValuePairs);
		doParse(content);
		return textTranslated.toString();
	}
}
