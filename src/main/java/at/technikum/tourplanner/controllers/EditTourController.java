package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EditTourController extends BaseTourController {
  @Override
  protected void onSaveButtonClicked() {
    tourTableViewModel.updateTour(tourViewModel);
    toursListView.setItems(tourTableViewModel.getDataNames());
    log.info(tourTableViewModel.getDataNames().toString());
    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}