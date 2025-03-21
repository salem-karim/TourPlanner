package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.LogViewModel;
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
    // Copy values back to the original model
    originalLogViewModel.setComment(logViewModel.getComment());
    originalLogViewModel.setDifficulty(logViewModel.getDifficulty());
    originalLogViewModel.setTotalDistance(logViewModel.getTotalDistance());
    originalLogViewModel.setTotalTime(logViewModel.getTotalTime());
    originalLogViewModel.setRating(logViewModel.getRating());
    originalLogViewModel.setDate(LocalDateTime.of(logViewModel.getDate(), LocalTime.now()));

    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}