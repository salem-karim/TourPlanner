package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@SuperBuilder
@Slf4j
public class NewLogController extends BaseLogController {
  @Override
  protected void onSaveButtonClicked() {
    logViewModel.idProperty().set(UUID.randomUUID());
    logTableViewModel.newLog(logViewModel);
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}