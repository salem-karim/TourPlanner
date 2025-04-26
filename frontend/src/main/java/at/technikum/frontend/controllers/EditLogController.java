package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.viewmodels.LogViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SuperBuilder
@Slf4j
public class EditLogController extends BaseLogController {
  private LogViewModel originalLogViewModel;

  @Override
  protected void onSaveButtonClicked() {
    if (originalLogViewModel == null) {
      log.error("originalLogViewModel is null");
      TourPlannerApplication.closeWindow(saveCancelButtonBar);
      return;
    }
   
    // Copy values back to the original model
    //TODO: Maybe use updateLog instead of setting each property
    originalLogViewModel.setComment(logViewModel.getComment());
    originalLogViewModel.setDifficulty(logViewModel.getDifficulty());
    originalLogViewModel.setTotalDistance(logViewModel.getTotalDistance());
    originalLogViewModel.setTotalTime(logViewModel.getTotalTime());
    originalLogViewModel.setRating(logViewModel.getRating());
    originalLogViewModel.setDate(LocalDateTime.of(logViewModel.getDate(), LocalTime.now()));

    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}