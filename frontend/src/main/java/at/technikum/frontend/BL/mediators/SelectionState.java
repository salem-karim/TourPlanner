package at.technikum.frontend.BL.mediators;

public enum SelectionState {
  NO_SELECTION,
  ONE_SELECTED,
  MANY_SELECTED;

  SelectionState() {
  }

  public static SelectionState fromCount(int count) {
    return switch (count) {
      case 0 -> NO_SELECTION;
      case 1 -> ONE_SELECTED;
      default -> MANY_SELECTED;
    };
  }
}
