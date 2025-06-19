package at.technikum.frontend.controllers;

import java.util.UUID;

import at.technikum.frontend.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class NewLogController extends BaseLogController {
  @Override
  protected void onSaveButtonClicked() {
    logViewModel.idProperty().set(UUID.randomUUID());
    logViewModel.tourIdProperty().set(selectedTour.getId());
    logTableViewModel.newLog(logViewModel);
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}
