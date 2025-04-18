package at.technikum.frontend.utils;


import static at.technikum.frontend.TourPlannerApplication.i18n;

public enum TransportType {
  CAR(i18n.getString("tourInfo.transportType.car")),
  BIKE(i18n.getString("tourInfo.transportType.bike")),
  FOOT(i18n.getString("tourInfo.transportType.foot")),
  BUS(i18n.getString("tourInfo.transportType.bus")),
  TRAIN(i18n.getString("tourInfo.transportType.train"));

  private final String transportType;

  TransportType(String string) {
    this.transportType = string;
  }

  public String get() {
    return transportType;
  }

  @Override
  public String toString() {
    return transportType;
  }
}
