package com.neuroleap.speachandlanguage.Listeners;

import com.neuroleap.speachandlanguage.Models.Screening;

/**
 * Created by Karl on 4/20/2015.
 */
public interface OnScreeningsListButtonsListener {

    public void onScreeningResultsButtonClicked(Screening screening);

    public void onScreeningOverviewButtonClicked(Screening screening);

    public void onScreeningQuestionsButtonClicked(Screening screening);

    public void onScreeningProfileButtonClicked(Screening screening);
}
