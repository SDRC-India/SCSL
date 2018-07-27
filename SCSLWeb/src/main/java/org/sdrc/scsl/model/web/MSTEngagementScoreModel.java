package org.sdrc.scsl.model.web;

/**
 * 
 * * This model will contain the list of Engagement Score for the submission of
 * the score of a facility within a timeperiod
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class MSTEngagementScoreModel {

	private Integer mstEngagementScoreId;

	private String progress;

	private String definition;

	public Integer getMstEngagementScoreId() {
		return mstEngagementScoreId;
	}

	public void setMstEngagementScoreId(Integer mstEngagementScoreId) {
		this.mstEngagementScoreId = mstEngagementScoreId;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	private float score;

}
