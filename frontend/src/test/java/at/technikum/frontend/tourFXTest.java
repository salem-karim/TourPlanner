package at.technikum.frontend;

import at.technikum.frontend.viewmodels.TourViewModel;
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

    assertThat(robot.window("New Tour")).isShowing();

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

    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    TourViewModel firstItem = listView.getItems().getFirst(); // Get the first item
    String firstItemName = firstItem.getName();

    Assertions.assertThat(firstItemName).isEqualTo("Schneeberg Wanderung");
  }

  @Test
  void testNameOfFirstListItem(FxRobot robot) {
    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    TourViewModel firstItem = listView.getItems().getFirst();
    String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Kahlsberg Wanderung");
  }

  @Test
  void testCreatenewTour(FxRobot robot) {


    robot.clickOn("#newButton");
    robot.sleep(500);
    robot.clickOn("#name");
    robot.write("Test Wanderung");

    robot.clickOn("#description");
    robot.write("Test Info");

    robot.clickOn("#from");
    robot.write("Test Start");

    robot.clickOn("#to");
    robot.write("Test Ende");

    robot.clickOn("#transportType");
    robot.clickOn("Car");

    robot.clickOn("#okButton");

    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    TourViewModel firstItem = listView.getItems().getLast();
    String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Test Wanderung");
  }

  @Test
  void testEditFirstTour(FxRobot robot) {


    robot.clickOn("#editButton");

    robot.clickOn("#name");
    robot.eraseText(50);
    robot.write("Andere Wanderung");

    robot.clickOn("#okButton");

    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    TourViewModel firstItem = listView.getItems().getFirst();
    String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Andere Wanderung");
  }

}
