package net;

public class Protocol {

    // Command constants
    public static final String SEARCH_COMMAND = "search";
    public static final String INSERT_COMMAND = "insert";
    public static final String BYE_COMMAND = "bye";
    public static final String UNKNOWN_COMMAND = "unknown";

    // Utility methods for formatting requests
    public static String createSearchRequest(String lastName) {
        return SEARCH_COMMAND + ":" + lastName;
    }

    public static String createInsertRequest(String firstName, String lastName, String school, int semester, int passedCourses) {
        return INSERT_COMMAND + ":" + String.join(",", firstName, lastName, school, String.valueOf(semester), String.valueOf(passedCourses));
    }

    public static String createByeRequest() {
        return BYE_COMMAND;
    }

    // Parsing methods to get the command and data
    public static String getCommand(String request) {
        if (request == null || request.isEmpty()) {
            return UNKNOWN_COMMAND;
        }
        return request.split(":", 2)[0].trim().toLowerCase();
    }

    public static String getCommandData(String request) {
        if (request == null || !request.contains(":")) {
            return null;
        }
        return request.split(":", 2)[1].trim();
    }
}