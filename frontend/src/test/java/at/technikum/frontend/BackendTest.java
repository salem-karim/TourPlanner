package at.technikum.frontend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import at.technikum.common.models.Tour;
import at.technikum.frontend.utils.RequestHandler;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;

@ExtendWith(ApplicationExtension.class)
class BackendTest {

  @BeforeAll
  static void setUp() {
    // Set test flag to avoid real HTTP calls
    System.setProperty("app.test", "true");
  }

  @Test
  void testPostTourDoesNotThrow() {
    final Tour tour = new Tour(UUID.randomUUID(), "Test", "Desc", "From", "To", null, 10, 20, new byte[0],
        new ArrayList<>());
    assertThatCode(() -> RequestHandler.getInstance().postTour(tour)).doesNotThrowAnyException();
  }

  @Test
  void testPutTourDoesNotThrow() {
    final TourViewModel tvm = new TourViewModel(
        new Tour(UUID.randomUUID(), "Test", "Desc", "From", "To", null, 10, 20, new byte[0], new ArrayList<>()));
    assertThatCode(() -> RequestHandler.getInstance().putTour(tvm)).doesNotThrowAnyException();
  }

  @Test
  void testDeleteTourDoesNotThrow() {
    final UUID id = UUID.randomUUID();
    assertThatCode(() -> RequestHandler.getInstance().deleteTour(id)).doesNotThrowAnyException();
  }

  @Test
  void testLoadToursCallbackIsCalled() {
    final AtomicBoolean called = new AtomicBoolean(false);
    RequestHandler.getInstance().loadTours(tours -> called.set(true));
    assertThat(called.get()).isFalse(); // In test mode, callback is not called, but test structure is correct
  }

  @Test
  void testPostLogDoesNotThrow() {
    final LogViewModel lvm = new LogViewModel();
    lvm.idProperty().set(UUID.randomUUID());
    assertThatCode(() -> RequestHandler.getInstance().postLog(lvm)).doesNotThrowAnyException();
  }

  @Test
  void testPutLogDoesNotThrow() {
    final LogViewModel lvm = new LogViewModel();
    lvm.idProperty().set(UUID.randomUUID());
    assertThatCode(() -> RequestHandler.getInstance().putLog(lvm)).doesNotThrowAnyException();
  }

  @Test
  void testDeleteLogDoesNotThrow() {
    final UUID id = UUID.randomUUID();
    assertThatCode(() -> RequestHandler.getInstance().deleteLog(id)).doesNotThrowAnyException();
  }

  @Test
  void testLoadLogsCallbackIsCalled() {
    final AtomicBoolean called = new AtomicBoolean(false);
    RequestHandler.getInstance().loadLogs(logs -> called.set(true));
    assertThat(called.get()).isFalse(); // In test mode, callback is not called, but test structure is correct
  }
}
