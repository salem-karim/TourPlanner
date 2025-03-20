package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EditTourController extends BaseTourController {
  private TourViewModel originalTourViewModel;

  public void setupWithTour(TourViewModel tourToEdit) {
    // Create a copy of the tour to edit
    this.originalTourViewModel = tourToEdit;
    this.tourViewModel = new TourViewModel(tourToEdit);
    initialize();
  }

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