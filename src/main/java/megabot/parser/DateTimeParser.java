package megabot.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import megabot.exception.MegabotException;

/**
 * Utility class for parsing and handling various datetime formats.
 * Provides robust datetime parsing with multiple format support and clear error messages.
 * @author Xu Yong Lin
 * @version 1.0
 */
public class DateTimeParser {

    // Comprehensive list of supported date formats
    private static final List<DateTimeFormatter> DATE_FORMATTERS = new ArrayList<>();
    private static final List<DateTimeFormatter> DATETIME_FORMATTERS = new ArrayList<>();

    static {
        // Date-only formats
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("d/M/yyyy"));
        DATE_FORMATTERS.add(DateTimeFormatter.ofPattern("d-M-yyyy"));

        // DateTime formats (date + time)
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
        DATETIME_FORMATTERS.add(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a"));
    }

    /**
     * Parses a datetime string with flexible format support.
     * First tries datetime formats, then falls back to date-only formats (with midnight time).
     *
     * @param dateTimeStr the datetime string to parse
     * @return LocalDateTime object
     * @throws MegabotException if the string cannot be parsed with any supported format
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws MegabotException {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Date/time cannot be empty.");
        }

        String trimmedInput = dateTimeStr.trim();

        // First try datetime formats
        for (DateTimeFormatter formatter : DATETIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmedInput.replace("T", " "), formatter);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }

        // Then try date-only formats (set time to midnight)
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmedInput, formatter);
                return LocalDateTime.of(date, LocalTime.MIDNIGHT);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }

        // If no format worked, throw detailed error
        throw new MegabotException(createDateTimeErrorMessage(trimmedInput));
    }

    /**
     * Parses a date string with flexible format support.
     *
     * @param dateStr the date string to parse
     * @return LocalDate object
     * @throws MegabotException if the string cannot be parsed with any supported format
     */
    public static LocalDate parseDate(String dateStr) throws MegabotException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Date cannot be empty.");
        }

        String trimmedInput = dateStr.trim();

        // Try all date formats
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(trimmedInput, formatter);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }

        // If no format worked, throw detailed error
        throw new MegabotException(createDateErrorMessage(trimmedInput));
    }

    /**
     * Creates a comprehensive error message for invalid datetime input.
     *
     * @param input the invalid input string
     * @return detailed error message with supported formats
     */
    private static String createDateTimeErrorMessage(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append("OOPSIE!! Invalid date/time format: '").append(input).append("'\n");
        sb.append("Supported formats include:\n");
        sb.append("• Date formats: 2024-12-25, 25/12/2024, 12/25/2024, 25 Dec 2024\n");
        sb.append("• DateTime formats: 2024-12-25 14:30, 25/12/2024 2:30 PM, 25 Dec 2024 14:30\n");
        sb.append("Please use one of these formats and try again.");
        return sb.toString();
    }

    /**
     * Creates a comprehensive error message for invalid date input.
     *
     * @param input the invalid input string
     * @return detailed error message with supported formats
     */
    private static String createDateErrorMessage(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append("OOPSIE!! Invalid date format: '").append(input).append("'\n");
        sb.append("Supported formats include:\n");
        sb.append("• 2024-12-25, 25/12/2024, 12/25/2024, 25-12-2024\n");
        sb.append("• 25 Dec 2024, Dec 25 2024, 25/12/2024\n");
        sb.append("Please use one of these formats and try again.");
        return sb.toString();
    }

    /**
     * Validates that a start date is not after an end date.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @throws MegabotException if start date is after end date
     */
    public static void validateDateRange(LocalDate startDate, LocalDate endDate) throws MegabotException {
        if (startDate.isAfter(endDate)) {
            throw new MegabotException("OOPSIE!! Start date cannot be after end date. "
                    + "Start: " + startDate + ", End: " + endDate);
        }
    }

    /**
     * Validates that a start datetime is not after an end datetime.
     *
     * @param startDateTime the start datetime
     * @param endDateTime the end datetime
     * @throws MegabotException if start datetime is after end datetime
     */
    public static void validateDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws MegabotException {
        if (startDateTime.isAfter(endDateTime)) {
            throw new MegabotException("OOPSIE!! Start time cannot be after end time. "
                    + "Start: " + startDateTime + ", End: " + endDateTime);
        }
    }
}
