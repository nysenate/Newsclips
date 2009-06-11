package gov.nysenate.newsclips;

import java.util.Date;

public class Quote {

	String quoteId;
	
	Date timestamp;
	
	String quoteText;
	
	String sourceName;
	
	String articleHeadline;
	
	String articleUrl;
}

/*
<response>
<message type="str">Success</message>
<code type="int4">2001</code>
<payload>
	<quote>
		<quote_id type="str">03TWezQa1M85p</quote_id>
		<timestamp_epoch type="int4">1200749352</timestamp_epoch>
		<timestamp type="str">2008-01-19 13:29:12</timestamp>
		<quote_text type="str">But we will still be friends with the 
					Americans and invite them to tea every 
					time they drive through the town
		</quote_text>
		<topic>
			<daylife_url type="str">http://www.daylife.com/topic/Condoleezza_Rice</daylife_url>
			<type type="str">PERSON</type>
			<name type="str">Condoleezza Rice</name>
			<topic_id type="str">01PQ0QsaGm49k</topic_id>
		</topic>
		<source>
			<name type="str">MSNBC</name>
			<url type="str">http://www.msnbc.msn.com/</url>
			<daylife_url type="str">http://www.daylife.com/source/MSNBC</daylife_url>
			<rank type="int4">1</rank>
			<source_id type="str">07yTb3kbhD6Bo</source_id>
			<type type="int4">1</type>
		</source>
		<daylife_url type="str">http://www.daylife.com/quote/03TWezQa1M85p</daylife_url>
		<article>
			<timestamp_epoch type="int4">1200749352</timestamp_epoch>
			<headline type="str">'Awakening' pacts prove complicated</headline>
			<timestamp type="str">2008-01-19 13:29:12</timestamp>
			<url type="str">http://www.msnbc.msn.com/id/22739412/</url>
			<daylife_url type="str">http://www.daylife.com/article/0baOdqcflg2hB</daylife_url>
			<article_id type="str">0baOdqcflg2hB</article_id>
		</article>
	</quote>
	........
	........
</payload>
</response>
*/