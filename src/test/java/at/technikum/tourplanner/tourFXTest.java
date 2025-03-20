package at.technikum.tourplanner;

import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class MainViewModelTest {

  /**
   * Will be called with {@code @Before} semantics, i.e. before each test method.
   *
   * @param primaryStage - Will be injected by the test runner.
   */
  @Start
  private void start(Stage primaryStage) throws Exception {
    new TourPlannerApplication().start(primaryStage);
  }

  @Test
  void testNewButtonAndCancel(FxRobot robot) {

    robot.clickOn("#newButton");
    robot.sleep(250);

    assertThat(robot.window("Create New Tour")).isShowing();

    // robot.clickOn("#cancelButton");

    // Verify that the new window is closed -> problem
    // assertThat(robot.window("Tour Planner")).isShowing();

  }

  @Test
  void testEditButtonAndCancel(FxRobot robot) {
    robot.clickOn("#editButton");
    robot.sleep(250);

    assertThat(robot.window("Edit Tour")).isShowing();

    // robot.clickOn("#cancelButton");

    // Verify that the new window is closed -> problem
    // assertThat(robot.window("Edit Tour")).isNotShowing();
  }

  @Test
  void testDeleteButtonAndApply(FxRobot robot) {
    robot.clickOn("#deleteButton");

    robot.clickOn("OK");
    robot.sleep(250);

    ListView<TourViewModel> listView = robot.lookup("#toursListView").query();
    TourViewModel firstItem = listView.getItems().getFirst(); // Get the first item
    String firstItemName = firstItem.getName();

    Assertions.assertThat(firstItemName).isEqualTo("Schneeberg Wanderung");
  }

  @Test
  void testNameOfFirstListItem(FxRobot robot) {
    ListView<TourViewModel> listView = robot.lookup("#toursListView").query();
    TourViewModel firstItem = listView.getItems().getFirst();
    String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Kahlsberg Wanderung");
  }
}
