package org.nysenatecio.clips;

import java.util.Date;

public class Clip implements Comparable<Clip> {

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Clip o) {
	
		return (o.getSourceName().compareTo(getSourceName()));
		
	}

	/**
	 * @return the matchingTerms
	 */
	public String getMatchingTerms() {
		return matchingTerms;
	}

	/**
	 * @param matchingTerms the matchingTerms to set
	 */
	public void setMatchingTerms(String matchingTerms) {
		this.matchingTerms = matchingTerms;
	}

	/**
	 * @return the fullStory
	 */
	public String getFullStory() {
		return fullStory;
	}

	/**
	 * @param fullStory the fullStory to set
	 */
	public void setFullStory(String fullStory) {
		this.fullStory = fullStory;
	}

	/**
	 * @return the articleId
	 */
	public String getArticleId() {
		return articleId;
	}

	/**
	 * @param articleId the articleId to set
	 */
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	/**
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * @param headline the headline to set
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the excerpt
	 */
	public String getExcerpt() {
		return excerpt;
	}

	/**
	 * @param excerpt the excerpt to set
	 */
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	public String toString ()
	{
		return getSourceName();
	}
	
	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @return the sourceRank
	 */
	public int getSourceRank() {
		return sourceRank;
	}

	/**
	 * @param sourceRank the sourceRank to set
	 */
	public void setSourceRank(int sourceRank) {
		this.sourceRank = sourceRank;
	}

	/**
	 * @return the sourceType
	 */
	public int getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the relevance
	 */
	public int getRelevance() {
		return relevance;
	}

	/**
	 * @param relevance the relevance to set
	 */
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}

	

	//headline
	public String headline;
	
	//timestamp
	public Date timestamp;
	
	//excerpt
	public String excerpt;
	
	//full
	public String fullStory;
	
	//source: name
	public String sourceName;
	
	//source: rank
	public int sourceRank;
	
	//source: type
	public int sourceType;
	
	//url
	public String url;
	
	//topic_story_relevance
	public int relevance;
	
	//article_id
	public String articleId;
	
	public String matchingTerms;
	
	
}
