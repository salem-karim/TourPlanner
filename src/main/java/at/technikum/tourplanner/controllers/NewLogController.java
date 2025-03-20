package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class NewLogController extends BaseLogController {
  @Override
  protected void onSaveButtonClicked() {
    log.info("Creating new log entry");
    logTableViewModel.newLog(logViewModel);
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}