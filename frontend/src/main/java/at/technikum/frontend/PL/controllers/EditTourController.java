package at.technikum.frontend.PL.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EditTourController extends BaseTourController {
  private final TourViewModel originalTourViewModel;

  @Override
  protected void onSaveButtonClicked() {
    if (originalTourViewModel == null) {
      log.error("originalTourViewModel is null");
      TourPlannerApplication.closeWindow(newCancelButtonBar);
      return;
    }

    // Copy values back to the original model
    tourTableViewModel.updateTour(tourViewModel, originalTourViewModel);
    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}
