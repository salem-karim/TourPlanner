package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.viewmodels.TourViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EditTourController extends BaseTourController {
  private TourViewModel originalTourViewModel;

  @Override
  protected void onSaveButtonClicked() {
    if (originalTourViewModel == null) {
      log.error("originalTourViewModel is null");
      TourPlannerApplication.closeWindow(newCancelButtonBar);
      return;
    }

    // Copy values back to the original model
    originalTourViewModel.setName(tourViewModel.getName());
    originalTourViewModel.setDescription(tourViewModel.getTour_description());
    originalTourViewModel.setFrom(tourViewModel.getFrom());
    originalTourViewModel.setTo(tourViewModel.getTo());
    originalTourViewModel.setTransportType(tourViewModel.getTransport_type());

    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}