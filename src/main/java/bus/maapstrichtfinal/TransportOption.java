package bus.maapstrichtfinal;

/**
 * Enum representing different transport options.
 */
public enum TransportOption {
    WALK("Walk"),
    BICYCLE("Bicycle"),
    BUS("Bus");

    private String option;

    /**
     * Constructor to initialize the transport option.
     *
     * @param option the transport option as a string
     */
    TransportOption(String option) {
        this.option = option;
    }

    /**
     * Gets the transport option as a string.
     *
     * @return the transport option
     */
    public String getOption() {
        return option;
    }

    /**
     * Converts a string to a TransportOption enum.
     *
     * @param option the transport option as a string
     * @return the corresponding TransportOption
     * @throws IllegalArgumentException if the transport option is unknown
     */
    public static TransportOption fromString(String option) {
        for (TransportOption transportOption : values()) {
            if (transportOption.getOption().equalsIgnoreCase(option)) {
                return transportOption;
            }
        }
        throw new IllegalArgumentException("Unknown transport option: " + option);
    }
}
