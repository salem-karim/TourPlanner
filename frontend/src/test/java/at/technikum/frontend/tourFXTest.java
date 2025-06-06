package at.technikum.frontend;

import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.assertions.api.Assertions.assertThat;

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
  private void start(Stage primaryStage) throws Exception {
    new TourPlannerApplication().start(primaryStage);
  }

  @Test
  void testNewButtonAndCancel(FxRobot robot) {

    robot.clickOn("#newButton");

    assertThat(robot.window("New Tour")).isShowing();

    robot.clickOn("#cancelButton");

    // Verify that the new window is closed
    assertThat(robot.window("Tour Planner")).isShowing();

  }

  @Test
  void testEditButtonAndCancel(FxRobot robot) {
    robot.clickOn("#editButton");

    assertThat(robot.window("Edit Tour")).isShowing();

    robot.clickOn("#cancelButton");

    // Check that "Edit Tour" window is not present in the list of open windows
    boolean editTourWindowOpen = robot.listWindows().stream()
            .filter(window -> window instanceof Stage)
            .map(window -> ((Stage) window).getTitle())
            .anyMatch(title -> "Edit Tour".equals(title));
    Assertions.assertThat(editTourWindowOpen).isFalse();
  }

  @Test
  void testDeleteButtonAndApply(FxRobot robot) {
    robot.clickOn("#deleteButton");

    robot.clickOn("OK");

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
    robot.clickOn("#name");
    robot.write("Test Wanderung");

    robot.clickOn("#description");
    robot.write("Test Info");

    robot.clickOn("#from");
    robot.write("Test Start");

    robot.clickOn("#to");
    robot.write("Test Ende");

    robot.clickOn("#transportType");
    // All transport types besides Car work as Car is also displayed in the Main Window
    robot.clickOn("Bike");

    robot.clickOn("#okButton");

    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    TourViewModel lastItem = listView.getItems().getLast();
    String lastItemName = lastItem.getName();

    assertThat(lastItemName).isEqualTo("Test Wanderung");
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

  @Test
  void testDeleteTourEntry(FxRobot robot) {
    ListView<TourViewModel> listView = robot.lookup("#tourListView").query();
    int initialSize = listView.getItems().size();

    robot.clickOn("#deleteButton");
    robot.clickOn("OK");

    int newSize = listView.getItems().size();
    Assertions.assertThat(newSize).isEqualTo(initialSize - 1);
  }

  // todo:all logs test dont work


  @Test
  void testCreateLog(FxRobot robot) {
    // Select a tour
    ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());

    // Go to logs page
    robot.clickOn("#LogsTab");

    // Create new log
    robot.clickOn("#newLogButton");

    robot.clickOn("#logComment");
    robot.write("Test log comment");
    
    //TODO: Still needs to set the date to be valid
    robot.clickOn("#logOkButton");

    ListView<?> logListView = robot.lookup("#logListView").query();
    boolean found = logListView.getItems().stream()
            .anyMatch(item -> item.toString().contains("Test log comment"));
    Assertions.assertThat(found).isTrue();
  }

  @Test
  void testEditLog(FxRobot robot) {
    ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    TableView<?> logTable = robot.lookup("#logTable").query();
    robot.interact(() -> logTable.getSelectionModel().selectFirst());

    robot.clickOn("#editLogButton");

    robot.clickOn("#logComment");
    robot.eraseText(50);
    robot.write("Edited log comment");
    robot.clickOn("#logOkButton");

    TableColumn<?, String> commentColumn = (TableColumn<?, String>) logTable.getColumns().stream()
            .filter(col -> "Comment".equals(col.getText()) || "comment".equals(col.getId()))
            .findFirst()
            .orElseThrow();

    String actualComment = commentColumn.getCellData(0);
    Assertions.assertThat(actualComment).isEqualTo("Edited log comment");
  }

  @Test
  void testDeleteLog(FxRobot robot) {
    ListView<TourViewModel> tourListView = robot.lookup("#tourListView").query();
    robot.interact(() -> tourListView.getSelectionModel().selectFirst());
    robot.clickOn("#LogsTab");

    TableView<?> logListView = robot.lookup("#logTable").query();
    int initialSize = logListView.getItems().size();

    robot.interact(() -> logListView.getSelectionModel().selectFirst());
    robot.clickOn("#deleteLogButton");
    robot.clickOn("OK");

    int newSize = logListView.getItems().size();
    Assertions.assertThat(newSize).isEqualTo(initialSize - 1);
  }

}
