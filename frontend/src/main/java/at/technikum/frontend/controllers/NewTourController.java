package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.viewmodels.LogTableViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.UUID;

@SuperBuilder
@Slf4j
public class NewTourController extends BaseTourController {
  @Override
  protected void onSaveButtonClicked() {
    tourViewModel.idProperty().set(UUID.randomUUID());
    tourViewModel.logsProperty().set(new LogTableViewModel(new ArrayList<>()));
    tourTableViewModel.newTour(tourViewModel);
    TourPlannerApplication.closeWindow(newCancelButtonBar);
  }
}