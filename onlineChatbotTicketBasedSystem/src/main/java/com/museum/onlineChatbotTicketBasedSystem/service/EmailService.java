package com.museum.onlineChatbotTicketBasedSystem.service;

import com.museum.onlineChatbotTicketBasedSystem.model.Orders;
import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.model.TicketDetails;
import com.museum.onlineChatbotTicketBasedSystem.utility.QRCodeGenerator;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTicketEmail(String toEmail, String subject, TicketDetails ticketDetails,
                                PersonalInfo personalInfo, Orders order) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);

        // Generate QR Code content and image
        String qrCodeData = "Ticket ID: " + personalInfo.getId() +
                "\nName: " + personalInfo.getFirstname() + " " + personalInfo.getLastname() +
                "\nemail: " + personalInfo.getEmail() +
                "\nphone_no: " + personalInfo.getPhoneNo();

        String qrCodeBase64 = QRCodeGenerator.generateQRCode(qrCodeData, 400, 400);

        // Build email content with embedded QR code image and enhanced styling
        String emailContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "  @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');" +
                        "</style>" +
                        "</head>" +
                        "<body style='font-family: Poppins, Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5;'>" +
                        "<div style='max-width: 700px; margin: 0 auto; background: white; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1);'>" +


                        // Header Section
                        "<div style='background: linear-gradient(135deg, #2c3e50, #3498db); padding: 30px; border-radius: 15px 15px 0 0; text-align: center; color: white;'>"+
                        " <img src='https://drive.google.com/uc?export=view&id=1WS3lV1dljDnSF0XT3KwamO8p7RFJtqg1'"+
                        " alt='Museum Logo'"+
                        " style='height: 60px; margin-bottom: 20px;'/>"+
                        "<h1 style='margin: 0; font-size: 28px; font-weight: 600;'>Digital Museum Ticket</h1>"+
                        "<p style='margin: 5px 0 0; opacity: 0.9;'>Thank you for your purchase!</p>"+
                        "</div>"+



                         // Ticket Details
                        "<div style='padding: 30px;'>" +
                        "<div style='display: flex; justify-content: space-between; margin-bottom: 25px;'>" +
                        "<div style='flex: 1; padding-right: 20px;'>" +
                        "<h2 style='color: #2c3e50; margin-top: 0; border-bottom: 2px solid #3498db; padding-bottom: 8px;'>Ticket Information</h2>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Ticket ID:</strong> " + ticketDetails.getId() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Date:</strong> " + ticketDetails.getDate() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Time Slot:</strong> " + ticketDetails.getTime() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Adult Tickets:</strong> " + ticketDetails.getAdultticket() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Child Tickets:</strong> " + ticketDetails.getChildticket() + "</p>" +
                        "</div>" +


                        // Ticket Payment order
                        "<div style='flex: 1; padding-left: 20px; border-left: 2px solid #eee;'>" +
                        "<h2 style='color: #2c3e50; margin-top: 0; border-bottom: 2px solid #3498db; padding-bottom: 8px;'>Payment Order</h2>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Amount Paid :</strong> " + order.getAmount() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Name:</strong> " + order.getName() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Email:</strong> " + order.getEmail() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Order Status:</strong> " + order.getOrderStatus() + "</p>" +
                        "<p style='margin: 12px 0;'><strong style='color: #3498db;'>Razorpay Transaction ID:</strong> " + order.getRazorpayOrderId() + "</p>" +
                        "</div>" +
                        "</div>" +



                        // QR Code Section
                        "<div style='margin-top: 30px; padding: 25px; background: #ffffff; border-radius: 12px; border: 2px solid #e0e0e0;'>" +
                        "<div style='text-align: center;'>" +
                        "<div style='display: inline-block; padding: 20px; background: #f8f9fa; border-radius: 8px;'>" +
                        "<h3 style='color: #2c3e50; margin: 0 0 15px 0; font-size: 20px;'>Entry QR Code</h3>" +
                        "<div style='padding: 15px; background: white; border-radius: 8px; display: inline-block; border: 1px solid #3498db;'>" +
                        "<img src='cid:qrCodeImage' alt='QR Code' style='width: 180px; height: 180px;'/>" +
                        "</div>" +
                        "<p style='margin: 20px 0 0; color: #666; font-size: 14px; line-height: 1.5;'>" +
                        "<strong>Scan instructions:</strong><br/>" +
                        "Present this QR code at museum entrance<br/>" +
                        "Valid for " + ticketDetails.getAdultticket() + " adults & " + ticketDetails.getChildticket() + " children" +
                        "</p>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +

                        "</div>" +
                        "</body>" +

                        // Footer
                        "<div style='margin-top: 30px; padding: 20px; background-color: #2c3e50; color: white; border-radius: 0 0 15px 15px; text-align: center; font-size: 14px;'>" +
                        "<p style='margin: 5px 0;'>Need help? Contact us at support@museum.com</p>" +
                        "<p style='margin: 5px 0;'>Museum Opening Hours: 9:00 AM - 7:00 PM Daily</p>" +
                        "<div style='margin-top: 15px;'>" +
                        "<a href='https://museum.com' style='color: #3498db; text-decoration: none; margin: 0 10px;'>Website</a>" +
                        "<a href='https://facebook.com/museum' style='color: #3498db; text-decoration: none; margin: 0 10px;'>Facebook</a>" +
                        "<a href='https://twitter.com/museum' style='color: #3498db; text-decoration: none; margin: 0 10px;'>Twitter</a>" +
                        "</div>" +
                        "</div>" +

                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(emailContent, true); // Enable HTML content

        // Attach the QR code image
        ByteArrayResource qrCodeResource = new ByteArrayResource(java.util.Base64.getDecoder().decode(qrCodeBase64));

        helper.addInline("qrCodeImage", qrCodeResource, "image/png");

        mailSender.send(message);
    }
}
