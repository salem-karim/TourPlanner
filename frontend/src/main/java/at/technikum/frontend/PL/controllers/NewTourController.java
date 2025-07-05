package at.technikum.frontend.PL.controllers;

import java.util.ArrayList;
import java.util.UUID;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.PL.viewmodels.LogTableViewModel;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

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
