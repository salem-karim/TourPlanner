package at.technikum.frontend.mediators;

public enum SelectionState {
  NO_SELECTION(0),
  ONE_SELECTED(1),
  MANY_SELECTED(2);

  private final int count;

  SelectionState(int count) {
    this.count = count;
  }

  public static SelectionState fromCount(int count) {
    return switch (count) {
      case 0 -> NO_SELECTION;
      case 1 -> ONE_SELECTED;
      default -> MANY_SELECTED;
    };
  }
}
