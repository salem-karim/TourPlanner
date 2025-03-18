//import java.util.concurrent.TimeoutException;
//
//import at.technikum.tourplanner.TourPlannerApplication;
//import at.technikum.tourplanner.controllers.TourPlannerController;
//import javafx.scene.Node;
//import javafx.scene.control.ListView;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.MouseButton;
//import org.junit.jupiter.api.*;
//import org.testfx.api.FxToolkit;
//import org.testfx.framework.junit5.ApplicationTest;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.testfx.util.WaitForAsyncUtils;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TourFXTests extends ApplicationTest {
//
//    private TourPlannerController controller;
//
//    @BeforeAll
//    public static void setupHeadlessMode() {
//        System.setProperty("testfx.robot", "glass");
//        System.setProperty("testfx.headless", "true");
//        System.setProperty("prism.order", "sw");
//        System.setProperty("prism.text", "t2k");
//        System.setProperty("java.awt.headless", "true");
//        System.setProperty("glass.platform", "Monocle");
//        System.setProperty("monocle.platform", "Headless");
//    }
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        FxToolkit.registerPrimaryStage();
//        FxToolkit.setupApplication(TourPlannerApplication::new);
//    }
//
//    @AfterEach
//    public void tearDown() throws TimeoutException {
//        FxToolkit.hideStage();
//        release(new KeyCode[]{});
//        release(new MouseButton[]{});
//    }
//
//    protected <T extends Node> T find(String query) {
//        return lookup(query).query();
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/main_window.fxml"));
//        Parent root = loader.load();
//        controller = loader.getController();
//
//        stage.setScene(new Scene(root));
//        stage.show();
//        stage.toFront();
//    }
//
//    @Test
//    public void testCreateTour() {
//        clickOn("#newButton");
//
//        clickOn("#tourNameField").write("Test Tour");
//        clickOn("#tourDescriptionField").write("Description");
//        clickOn("#fromLocationField").write("Vienna");
//        clickOn("#toLocationField").write("Salzburg");
//        clickOn("#transportTypeBox").clickOn("Car");
//        clickOn("#createButton");
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        ListView<String> toursListView = find("#toursListView");
//        assertTrue(toursListView.getItems().contains("Test Tour"), "New tour should be added to the list");
//    }
//
//    @Test
//    public void testEditTour() {
//        clickOn("#editButton");
//
//        clickOn("#tourNameField").eraseText(5).write("Updated Tour");
//        clickOn("#saveButton");
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        ListView<String> toursListView = find("#toursListView");
//        assertTrue(toursListView.getItems().contains("Updated Tour"), "Tour name should be updated in the list");
//    }
//
//    @Test
//    public void testDeleteTour() {
//        ListView<String> toursListView = find("#toursListView");
//
//        if (!toursListView.getItems().isEmpty()) {
//            clickOn(toursListView.lookup(".list-cell"));  // Select first item
//            clickOn("#deleteButton");
//
//            WaitForAsyncUtils.waitForFxEvents();
//
//            assertTrue(toursListView.getItems().isEmpty(), "Tour list should be empty after deletion");
//        }
//    }
//
//    @Test
//    public void testUIElementsExist() {
//        assertNotNull(find("#toursListView"), "Tours list should be present");
//        assertNotNull(find("#newButton"), "New button should be present");
//        assertNotNull(find("#editButton"), "Edit button should be present");
//        assertNotNull(find("#deleteButton"), "Delete button should be present");
//    }
//}
