package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.viewmodels.LogViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EditLogController extends BaseLogController {
  private final LogViewModel originalLogViewModel;

  @Override
  protected void onSaveButtonClicked() {
    if (originalLogViewModel == null) {
      log.error("originalLogViewModel is null");
      TourPlannerApplication.closeWindow(saveCancelButtonBar);
      return;
    }

    // Set the values back to the original model
    logTableViewModel.updateLog(logViewModel, originalLogViewModel);
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}
