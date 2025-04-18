package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
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
    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}