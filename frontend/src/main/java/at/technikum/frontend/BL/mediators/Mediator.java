package at.technikum.frontend.BL.mediators;

import java.util.Map;

public interface Mediator {
  default void validateButtonStates(Map<SelectionState, Boolean> buttonStates) {
    if (!buttonStates.keySet().containsAll(java.util.Arrays.asList(SelectionState.values()))) {
      throw new IllegalArgumentException("All selection states must be configured");
    }
  }
}
