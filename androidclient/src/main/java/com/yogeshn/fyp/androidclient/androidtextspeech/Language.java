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

@SuppressWarnings("CanBeFinal")
public enum Language {

	AFRIKAANS("af"), ALBANIAN("sq"), ARABIC("ar"), ARMENIAN("hy"), AZERBAIJANI(
			"az"), BASQUE("eu"), BELARUSIAN("be"), BENGALI("bn"), BULGARIAN(
			"bg"), CATALAN("ca"), CHINESE("zh-CN"), CROATIAN("hr"), CZECH("cs"), DANISH(
			"da"), DUTCH("nl"), ENGLISH("en"), ESTONIAN("et"), FILIPINO("tl"), FINNISH(
			"fi"), FRENCH("fr"), GALICIAN("gl"), GEORGIAN("ka"), GERMAN("de"), GREEK(
			"el"), GUJARATI("gu"), HAITIAN_CREOLE("ht"), HEBREW("iw"), HINDI(
			"hi"), HUNGARIAN("hu"), ICELANDIC("is"), INDONESIAN("id"), IRISH(
			"ga"), ITALIAN("it"), JAPANESE("ja"), KANNADA("kn"), KOREAN("ko"), LATIN(
			"la"), LATVIAN("lv"), LITHUANIAN("lt"), MACEDONIAN("mk"), MALAY(
			"ms"), MALTESE("mt"), NORWEGIAN("no"), PERSIAN("fa"), POLISH("pl"), PORTUGUESE(
			"pt"), PORTUGUESE_BR("pt-BR"), ROMANIAN("ro"), RUSSIAN("ru"), SERBIAN(
			"sr"), SLOVAK("sk"), SLOVENIAN("sl"), SPANISH("es"), SWAHILI("sw"), SWEDISH(
			"sv"), TAMIL("ta"), TELUGU("te"), THAI("th"), TURKISH("tr"), UKRAINIAN(
			"uk"), URDU("ur"), VIETNAMESE("vi"), WELSH("cy"), YIDDISH("yi"), CHINESE_SIMPLIFIED(
			"zh-CN"), CHINESE_TRADITIONAL("zh-TW");
	private String prefix;

	private Language(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public static Language fromString(String prefix){
		for(Language l : Language.values()){
			if(l.getPrefix().equalsIgnoreCase(prefix)){
				return l;
			}
		}
		return null;
	}

}
