package at.technikum.frontend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.assertions.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class MainViewModelTest {

  @BeforeAll
  static void setUp() {

    // Set up test flag
    System.setProperty("app.test", "true");

    // Set up the system properties for headless testing
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      return;
    }
    System.setProperty("testfx.robot", "glass");
    System.setProperty("testfx.headless", "true");
    System.setProperty("prism.order", "sw");
    System.setProperty("prism.text", "t2k");
    System.setProperty("java.awt.headless", "true");
    System.setProperty("glass.platform", "Monocle");
    System.setProperty("monocle.platform", "Headless");
  }

  /**
   * Will be called with {@code @Before} semantics, i.e. before each test method.
   *
   * @param primaryStage - Will be injected by the test runner.
   */
  @Start
  private void start(final Stage primaryStage) throws Exception {
    new TourPlannerApplication().start(primaryStage);
  }

  @Test
  void testNewButtonAndCancel(final FxRobot robot) {

    robot.clickOn("#newButton");

    assertThat(robot.window("New Tour")).isShowing();

    robot.clickOn("#cancelButton");

    // Verify that the new window is closed
    assertThat(robot.window("Tour Planner")).isShowing();

  }

  @Test
  void testEditButtonAndCancel(final FxRobot robot) {
    robot.clickOn("#editButton");

    assertThat(robot.window("Edit Tour")).isShowing();

    robot.clickOn("#cancelButton");

    // Check that "Edit Tour" window is not present in the list of open windows
    final boolean editTourWindowOpen = robot.listWindows().stream()
        .filter(window -> window instanceof Stage)
        .map(window -> ((Stage) window).getTitle())
        .anyMatch(title -> "Edit Tour".equals(title));
    Assertions.assertThat(editTourWindowOpen).isFalse();
  }

  @Test
  void testDeleteButtonAndApply(final FxRobot robot) {
    robot.clickOn("#deleteButton");

    robot.clickOn("OK");

    final ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    final TourViewModel firstItem = listView.getItems().getFirst(); // Get the first item
    final String firstItemName = firstItem.getName();

    Assertions.assertThat(firstItemName).isEqualTo("Schneeberg Wanderung");
  }

  @Test
  void testNameOfFirstListItem(final FxRobot robot) {
    final ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    final TourViewModel firstItem = listView.getItems().getFirst();
    final String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Kahlsberg Wanderung");
  }

  @Test
  void testCreatenewTour(final FxRobot robot) {
    robot.clickOn("#newButton");
    robot.clickOn("#name");
    robot.write("Test Wanderung");

    robot.clickOn("#description");
    robot.write("Test Info");

    robot.clickOn("#from");
    robot.write("Test Start");

    robot.clickOn("#to");
    robot.write("Test Ende");

    robot.clickOn("#transportType");
    // All transport types besides Car work as Car is also displayed in the Main
    // Window
    robot.clickOn("Bike");

    robot.clickOn("#okButton");

    final ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    final TourViewModel lastItem = listView.getItems().getLast();
    final String lastItemName = lastItem.getName();

    assertThat(lastItemName).isEqualTo("Test Wanderung");
  }

  @Test
  void testEditFirstTour(final FxRobot robot) {

    robot.clickOn("#editButton");

    robot.clickOn("#name");
    robot.eraseText(50);
    robot.write("Andere Wanderung");

    robot.clickOn("#okButton");

    final ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    final TourViewModel firstItem = listView.getItems().getFirst();
    final String firstItemName = firstItem.getName();

    assertThat(firstItemName).isEqualTo("Andere Wanderung");
  }

  @Test
  void testDeleteTourEntry(final FxRobot robot) {
    final ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    final int initialSize = listView.getItems().size();

    robot.clickOn("#deleteButton");
    robot.clickOn("OK");

    final int newSize = listView.getItems().size();
    Assertions.assertThat(newSize).isEqualTo(initialSize - 1);
  }

  // todo:testCreateLog has problems with Datepicker -> cant set date manually
  @Test
  void testCreateLog(final FxRobot robot) {
    // Select a tour
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());

    // Go to logs page
    robot.clickOn("#LogsTab");

    // Create new log
    robot.clickOn("#newLogButton");

    // Set the date (assuming DatePicker)
    robot.clickOn("#logStartDate");
    robot.write("01.01.2024"); // Use the correct format for your DatePicker
    robot.clickOn("#logEndDate");
    robot.write("02.01.2024"); // Use the correct format for your DatePicker

    // Fill required fields
    robot.clickOn("#logComment");
    robot.write("Test log comment");

    robot.clickOn("#logTotalDistance");
    robot.write("30"); // Use the correct format for your DatePicker

    robot.clickOn("#logOkButton");

    // Now check if the log was added
    final TableView<?> logTable = robot.lookup("#logTable").query();
    final boolean found = logTable.getItems().stream()
        .anyMatch(item -> item.toString().contains("Test log comment"));
    Assertions.assertThat(found).isTrue();
  }

  @Test
  void testEditLog(final FxRobot robot) {
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    final TableView<?> logTable = robot.lookup("#logTable").query();
    robot.interact(() -> logTable.getSelectionModel().selectFirst());

    robot.clickOn("#editLogButton");

    robot.clickOn("#logComment");
    robot.eraseText(50);
    robot.write("Edited log comment");
    robot.clickOn("#logOkButton");

    final TableColumn<?, String> commentColumn = (TableColumn<?, String>) logTable.getColumns().stream()
        .filter(col -> "Comment".equals(col.getText()) || "comment".equals(col.getId()))
        .findFirst()
        .orElseThrow();

    final String actualComment = commentColumn.getCellData(0);
    Assertions.assertThat(actualComment).isEqualTo("Edited log comment");
  }

  @Test
  void testEditLogAndCancel(final FxRobot robot) {
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    final TableView<?> logTable = robot.lookup("#logTable").query();
    robot.interact(() -> logTable.getSelectionModel().selectFirst());

    robot.clickOn("#editLogButton");

    robot.clickOn("#logComment");
    robot.eraseText(50);
    robot.write("Edited log comment");
    robot.clickOn("#logCancelButton");

    final TableColumn<?, String> commentColumn = (TableColumn<?, String>) logTable.getColumns().stream()
        .filter(col -> "Comment".equals(col.getText()) || "comment".equals(col.getId()))
        .findFirst()
        .orElseThrow();

    final String actualComment = commentColumn.getCellData(0);
    Assertions.assertThat(actualComment).isEqualTo("Great weather, enjoyed the hike!");
  }

  @Test
  void testEditMultipleLogsAndFail(final FxRobot robot) {
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    final TableView<?> logTable = robot.lookup("#logTable").query();
    // Select multiple logs
    robot.interact(() -> logTable.getSelectionModel().selectIndices(0, 1));

    // Check that the edit button is disabled
    final javafx.scene.control.Button editLogButton = robot.lookup("#editLogButton").queryButton();
    assertThat(editLogButton.isDisabled()).isTrue();
  }

  @Test
  void testDeleteLog(final FxRobot robot) {
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    final TableView<?> logListView = robot.lookup("#logTable").query();
    final int initialSize = logListView.getItems().size();

    robot.interact(() -> logListView.getSelectionModel().selectFirst());
    robot.clickOn("#deleteLogButton");
    robot.clickOn("OK");

    final int newSize = logListView.getItems().size();
    Assertions.assertThat(newSize).isEqualTo(initialSize - 1);
  }

  @Test
  void testDeleteMultipleLogs(final FxRobot robot) {
    final ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();

    robot.interact(() -> tourListView.getSelectionModel().selectFirst());

    robot.clickOn("#LogsTab");

    final TableView<?> logListView = robot.lookup("#logTable").query();
    final int initialSize = logListView.getItems().size();

    robot.interact(() -> logListView.getSelectionModel().selectIndices(0, 1));
    robot.clickOn("#deleteLogButton");
    robot.clickOn("OK");

    final int newSize = logListView.getItems().size();
    Assertions.assertThat(newSize).isEqualTo(initialSize - 2);
  }
}
