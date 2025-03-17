//import static org.testfx.api.FxAssert.verifyThat;
//import static org.testfx.matcher.control.LabeledMatchers.hasText;
//
//import at.technikum.tourplanner.controllers.TourPlannerController;
//import at.technikum.tourplanner.models.Tour;
//import at.technikum.tourplanner.viewmodels.TourTableViewModel;
//import at.technikum.tourplanner.viewmodels.TourViewModel;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testfx.framework.junit5.ApplicationTest;
//
//public class TourFXTests extends ApplicationTest {
//
//    private TourTableViewModel ttvm; // or TourViewModel??
//
//    // todo
//    @Override
//    public void start(Stage stage) {
//        ttvm = new TourTableViewModel();
//        TourPlannerController controller = new TourPlannerController(ttvm);
//        stage.setScene(controller.getScene());
//        stage.show();
//    }
//
//    // testing test
//    @Test
//    void testButtonClickUpdatesViewModel() {
//        clickOn("#myButton");  // Target button by ID
//        verifyThat("#myLabel", hasText("Updated!"));  // Verify the label updated
//    }
//
//    @Test
//    void testButtonClickOpensCreateWindow() {
//
//    }
//
//    @Test
//    void testButtonClickOpensEditWindow() {
//
//    }
//
//    @Test
//    void testButtonClickOpensLogsWindow() {
//
//    }
//
//    @Test
//    void testButtonClickOpensGeneralWindow() {
//
//    }
//}
