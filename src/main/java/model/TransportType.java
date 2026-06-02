package model;

public enum TransportType {
    TREIN("Trein"),
    BUS("Bus");

    private final String displayName;

    TransportType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
