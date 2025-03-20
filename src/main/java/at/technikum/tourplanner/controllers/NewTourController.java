package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@SuperBuilder
@Slf4j
public class NewTourController extends BaseTourController {
  @Override
  protected void onSaveButtonClicked() {
    tourViewModel.idProperty().set(UUID.randomUUID());
    tourTableViewModel.newTour(tourViewModel);
    toursListView.setItems(tourTableViewModel.getDataNames());
    log.info(tourTableViewModel.getDataNames().toString());
    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}