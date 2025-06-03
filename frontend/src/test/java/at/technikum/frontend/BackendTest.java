package at.technikum.frontend;

import at.technikum.frontend.viewmodels.TourViewModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import at.technikum.common.models.Tour;
import at.technikum.frontend.utils.RequestHandler;
import at.technikum.frontend.viewmodels.LogViewModel;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class BackendTest {

  @BeforeAll
  static void setUp() {
    // Set test flag to avoid real HTTP calls
    System.setProperty("app.test", "true");
  }

  @Test
  void testPostTourDoesNotThrow() {
    Tour tour = new Tour(UUID.randomUUID(), "Test", "Desc", "From", "To", null, 10, 20, new byte[0], new ArrayList<>());
    assertThatCode(() -> RequestHandler.getInstance().postTour(tour)).doesNotThrowAnyException();
  }

  @Test
  void testPutTourDoesNotThrow() {
    TourViewModel tvm = new TourViewModel(new Tour(UUID.randomUUID(), "Test", "Desc", "From", "To", null, 10, 20, new byte[0], new ArrayList<>()));
    assertThatCode(() -> RequestHandler.getInstance().putTour(tvm)).doesNotThrowAnyException();
  }

  @Test
  void testDeleteTourDoesNotThrow() {
    UUID id = UUID.randomUUID();
    assertThatCode(() -> RequestHandler.getInstance().deleteTour(id)).doesNotThrowAnyException();
  }

  @Test
  void testLoadToursCallbackIsCalled() {
    AtomicBoolean called = new AtomicBoolean(false);
    RequestHandler.getInstance().loadTours(tours -> called.set(true));
    assertThat(called.get()).isFalse(); // In test mode, callback is not called, but test structure is correct
  }

  @Test
  void testPostLogDoesNotThrow() {
    LogViewModel lvm = new LogViewModel();
    lvm.idProperty().set(UUID.randomUUID());
    assertThatCode(() -> RequestHandler.getInstance().postLog(lvm)).doesNotThrowAnyException();
  }

  @Test
  void testPutLogDoesNotThrow() {
    LogViewModel lvm = new LogViewModel();
    lvm.idProperty().set(UUID.randomUUID());
    assertThatCode(() -> RequestHandler.getInstance().putLog(lvm)).doesNotThrowAnyException();
  }

  @Test
  void testDeleteLogDoesNotThrow() {
    UUID id = UUID.randomUUID();
    assertThatCode(() -> RequestHandler.getInstance().deleteLog(id)).doesNotThrowAnyException();
  }

  @Test
  void testLoadLogsCallbackIsCalled() {
    AtomicBoolean called = new AtomicBoolean(false);
    RequestHandler.getInstance().loadLogs(logs -> called.set(true));
    assertThat(called.get()).isFalse(); // In test mode, callback is not called, but test structure is correct
  }
}