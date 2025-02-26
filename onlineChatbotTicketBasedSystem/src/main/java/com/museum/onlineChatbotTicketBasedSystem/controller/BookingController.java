package com.museum.onlineChatbotTicketBasedSystem.controller;

import com.museum.onlineChatbotTicketBasedSystem.Interface.BookingService;
import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.model.TicketDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dialogflow")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> handleBooking(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {

            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            if(queryResult == null) {
                return buildErrorResponse("Missing queryResult in request");
            }

            Map<String, Object> intentMap = (Map<String, Object>) queryResult.get("intent");
            if(intentMap == null) {
                return buildErrorResponse("No intent found in request");
            }

            String intentName = (String) intentMap.get("displayName");
            if(intentName == null || intentName.isEmpty()) {
                return buildErrorResponse("Glad you liked it. Let me know if there's anything else I can improve.");
            }

            Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");

            // Add debug logging
            System.out.println("Processing intent: " + intentName);
            System.out.println("Raw parameters: " + parameters);

            switch(intentName) {  // Handle case variations
                case "english":
                    handlePersonalInfo(parameters, response);
                    break;

                case "proceed with booking":
                    handleTicketDetails(parameters, response);
                    break;

                case "Modify":
                    handleTicketModification(parameters, response);
                    break;

                case "confirm cancellation":
                    handleTicketCancellation(parameters, response);
                    break;

                default:
                    response.put("fulfillmentText",
                            "I didn't understand that request. Please try again.");
            }

        } catch (DateTimeParseException e) {
            return buildErrorResponse("Invalid date/time format. Use: YYYY-MM-DDTHH:MM:SS+ZZ:ZZ");
        } catch (NumberFormatException e) {
            return buildErrorResponse("Please enter valid numbers for tickets");
        } catch (Exception e) {
            return buildErrorResponse("Processing error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);

    }

    // ------------------- Handling the User Personal Details ----------------------------------
    private void handlePersonalInfo(Map<String, Object> parameters, Map<String, Object> response) {

        // Extract parameters using Dialogflow's names
        List<String> requiredParams = Arrays.asList(
                "first_name",
                "last_name",
                "email",
                "phone_no",
                "date_of_birth",
                "address",
                "identification_details"
        );

        List<String> missing = requiredParams.stream()
                .filter(param -> parameters.get(param) == null)
                .collect(Collectors.toList());

        if(!missing.isEmpty()) {
            response.put("fulfillmentText", "Missing: " + String.join(", ", missing));
            return;
        }

        PersonalInfo info = new PersonalInfo();
        info.setFirstname(getStringParameter(parameters, "first_name"));
        info.setLastname(getStringParameter(parameters, "last_name"));
        info.setEmail(getStringParameter(parameters, "email"));
        info.setPhoneNo(parsePhoneNumber(parameters.get("phone_no")));


        boolean saved = bookingService.savePersonalInfo(info);

        response.put("fulfillmentText", saved ?
                "Your details have been successfully saved.\n" +
                        "\nChoose the below options Mr/Mrs " + info.getFirstname() + " " + info.getLastname() +
                        "\n1. Book Tickets\n" +
                        "\n2. Ticket Cancellation" :
                "Failed to save information. Please try again.");
    }



    // ------------------------------ Handling the User Tickets Details ----------------------------
    private void handleTicketDetails(Map<String, Object> parameters, Map<String, Object> response) {

        try {

            // Add parameter logging
            System.out.println("Ticket Params RAW: " + parameters);

            // Validate required parameters
            List<String> requiredParams = Arrays.asList("date",
                    "time",
                    "adult_tickets",
                    "child_tickets");

            List<String> missing = validateParameters(parameters, requiredParams);

            if (!missing.isEmpty()) {
                response.put("fulfillmentText", "❌ Missing: " + String.join(", ", missing));
                return;
            }

            // Create ticket
            TicketDetails details = new TicketDetails();
            details.setDate(parseLocalDate(getStringParameter(parameters, "date")));
            details.setTime(parseLocalTime(getStringParameter(parameters, "time")));
            details.setAdultticket(getIntegerParameter(parameters, "adult_tickets"));
            details.setChildticket(getIntegerParameter(parameters, "child_tickets"));

            // Save to DB
            boolean saved = bookingService.saveTicketDetails(details);

            response.put("fulfillmentText", saved ?
                    "✅ Booking confirmed!" + "" + "\nDo you have any Quries" +
                            "\n1. Yes" +
                            "\n2. No" :
                    "❌ Failed to save booking");

            // Verify values before saving
            System.out.println("Date: " + details.getDate());
            System.out.println("Time: " + details.getTime());
            System.out.println("Adult Tickets: " + details.getAdultticket());
            System.out.println("Child Tickets: " + details.getChildticket());

        } catch (DateTimeParseException e) {
            response.put("fulfillmentText", "⏰ Please use valid formats:\n"
                    + "Date: YYYY-MM-DD (e.g., 2025-02-14)\n"
                    + "Time: HH:MM (e.g., 14:30)");
        } catch (NumberFormatException e) {
            response.put("fulfillmentText", "🔢 Please enter valid numbers");
        } catch (Exception e) {
            response.put("fulfillmentText", "🚨 Error saving tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ------------------- Ticket Modification Handler -------------------
    private void handleTicketModification(Map<String, Object> parameters, Map<String, Object> response) {

        try {
            // Mandatory ID parameter
            Integer ticketId = getIntegerParameter(parameters, "id");
            if(ticketId == null) {
                response.put("fulfillmentText", "❌ Ticket ID is required for modification");
                return;
            }

            // Fetch existing ticket
            TicketDetails existingTicket = bookingService.getTicketById(ticketId);
            if(existingTicket == null) {
                response.put("fulfillmentText", "❌ Ticket not found with ID: " + ticketId);
                return;
            }

            // Update fields if provided
            boolean hasUpdates = false;

            if(parameters.containsKey("date")) {
                existingTicket.setDate(parseLocalDate(getStringParameter(parameters, "date")));
                hasUpdates = true;
            }

            if(parameters.containsKey("time")) {
                existingTicket.setTime(parseLocalTime(getStringParameter(parameters, "time")));
                hasUpdates = true;
            }

            if(parameters.containsKey("adult_tickets")) {
                existingTicket.setAdultticket(getIntegerParameter(parameters, "adult_tickets"));
                hasUpdates = true;
            }

            if(parameters.containsKey("child_tickets")) {
                existingTicket.setChildticket(getIntegerParameter(parameters, "child_tickets"));
                hasUpdates = true;
            }

            if(!hasUpdates) {
                response.put("fulfillmentText", "ℹ️ No changes provided for ticket ID: " + ticketId);
                return;
            }

            // Save updates
            boolean updated = bookingService.updateTicketDetails(existingTicket);
            response.put("fulfillmentText", updated ?
                    "✅ Ticket ID " + ticketId + " updated successfully!"+
                            "\nGreat! your booking is modified now. Do you have any other queries"+
                            "\nPlease choose one of the options below:"+
                            "\n1. Yes"+
                            "\n2. No" :
                    "❌ Failed to update ticket ID " + ticketId);

        } catch (DateTimeParseException e) {
            response.put("fulfillmentText", "📅 Invalid date/time format: " + e.getMessage());
        } catch (Exception e) {
            response.put("fulfillmentText", "⚠️ Error updating ticket: " + e.getMessage());
        }
    }


  // ------------------- Ticket confirm cancellation Handler -------------------
    private void handleTicketCancellation(Map<String, Object> parameters, Map<String, Object> response) {
        
        try {
            // Get ticket ID from parameters
            Integer ticketId = getIntegerParameter(parameters, "id");

            if(ticketId == null) {
                response.put("fulfillmentText", "❌ Ticket ID is required for cancellation");
                return;
            }

            // Convert to Long if needed (based on your entity ID type)
            Long id = ticketId.longValue();

            // Perform cancellation
            boolean success = bookingService.deleteTicketById(id);

            response.put("fulfillmentText", success ?
                    "✅ Ticket ID : " + id + "\n Your ticket has been successfully canceled. " +
                            "\nA cancellation charge of ₹10 has been deducted as per our policy." +
                            "\nThe refund amount of ₹40 will be credited to your original payment method within 7 working days." +
                            "\nThank you for using our service. If you have further queries, feel free to reach out." :
                    "❌ No ticket found with ID: " + id);

        } catch (NumberFormatException e) {
            response.put("fulfillmentText", "🔢 Invalid ticket ID format");
        } catch (Exception e) {
            response.put("fulfillmentText", "⚠️ Error cancelling ticket: " + e.getMessage());
        }
    }





    // Helper methods
    private List<String> validateParameters(Map<String, Object> parameters, List<String> requiredParams) {
        return requiredParams.stream()
                .filter(param -> parameters.get(param) == null)
                .collect(Collectors.toList());
    }

    private String getStringParameter(Map<String, Object> parameters, String paramName) {
        Object value = parameters.get(paramName);
        return value != null ? value.toString().trim() : null;
    }

    private Integer getIntegerParameter(Map<String, Object> parameters, String paramName) {
        Object value = parameters.get(paramName);

        if (value == null) return null;

        try {
            if (value instanceof Double) {
                return ((Double) value).intValue();
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
            return (Integer) value;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number for " + paramName + ": " + value);
        }
    }

    private String parsePhoneNumber(Object phoneNo) {
        if(phoneNo == null) return null;
        // Handle different formats: "+1 (234) 567-8900" -> "1234567890"
        return phoneNo.toString().replaceAll("[^0-9+]", "");
    }


    // Updated Date Parser
    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            // Handle both date-only and datetime formats
            return LocalDate.parse(dateStr.split("T")[0]);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format. Use YYYY-MM-DD", dateStr, 0);
        }
    }

    // Updated Time Parser
    private LocalTime parseLocalTime(String timeStr) {
        if (timeStr == null) return null;
        try {
            // Handle both time-only and datetime formats
            String timePart = timeStr.contains("T")
                    ? timeStr.split("T")[1].split("\\+")[0]
                    : timeStr;
            return LocalTime.parse(timePart);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid time format. Use HH:MM", timeStr, 0);
        }
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("📅 Date cannot be in the past");
        }
        if (time.isBefore(LocalTime.of(8, 0)) || time.isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("⏰ We're open 8 AM - 10 PM");
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Thank you! " + message);
        System.err.println("ERROR: " + message);  // Add logging
        return ResponseEntity.ok(response);
    }

}