/*
 * This code is currently released under the Mozilla Public License.
 * http://www.mozilla.org/MPL/
 */
package org.nysenatecio.clips;

import java.io.StreamTokenizer;

public class HtmlStreamTokenizer extends StreamTokenizer {

	public HtmlStreamTokenizer (java.io.Reader reader)
	{
		super (reader);
		
		resetSyntax();
        wordChars('A', 'Z');
        wordChars('a', 'z');
        wordChars('0', '9');
        wordChars('/','/');
        wordChars('.','.');
        wordChars('-','-');
        wordChars('&','&');
        wordChars('+','+');
        wordChars('=','=');
        wordChars('%','%');
        wordChars('?','?');
        wordChars('_','_');
        wordChars(':',':');
        wordChars('*','*');
	}
}
