//package at.technikum.tourplanner;
//
//import javafx.stage.Stage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.testfx.api.FxRobot;
//import org.testfx.framework.junit5.ApplicationExtension;
//import org.testfx.framework.junit5.Start;
//
//import static org.testfx.assertions.api.Assertions.assertThat;
//
//@ExtendWith(ApplicationExtension.class)
//class MainViewModelTest {
//
//  /**
//   * Will be called with {@code @Before} semantics, i.e. before each test method.
//   *
//   * @param primaryStage - Will be injected by the test runner.
//   */
//  @Start
//  private void start(Stage primaryStage) throws Exception {
//    new TourPlannerApplication().start(primaryStage);
//  }
//
//  @Test
//  void testNewButtonAndCancel(FxRobot robot) {
//    // Click the "New" button
//    robot.clickOn("#newButton");
//
//    // Verify that the new window is opened
//    assertThat(robot.window("Create New Tour")).isShowing();
//
//    // Click the "Cancel" button
//    robot.clickOn("#cancelButton");
//
//    // Verify that the new window is closed
//    assertThat(robot.window("Create New Tour")).isNotShowing();
//
//  }
//}