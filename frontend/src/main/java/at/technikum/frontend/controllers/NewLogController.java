package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@SuperBuilder
@Slf4j
public class NewLogController extends BaseLogController {
  @Override
  protected void onSaveButtonClicked() {
    logViewModel.idProperty().set(UUID.randomUUID());
    logViewModel.tourProperty().set(selectedTour);
    logTableViewModel.newLog(logViewModel);
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}