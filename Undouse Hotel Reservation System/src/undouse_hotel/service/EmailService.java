package undouse_hotel.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EmailService {
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "qjpcromualdo@tip.edu.ph";
    private static final String SENDER_PASSWORD = "gggu phva ykkv utfx"; 
    private static final String HOTEL_NAME = "Undouse Hotel";
    
    public static boolean sendBookingConfirmation(
            String guestEmail, 
            String guestName,
            String receiptNumber,
            String roomType,
            String checkInDate,
            String checkOutDate,
            double totalAmount,
            String paymentMethod,
            int roomCount,
            int guestCount) {
        
        try {
        
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, HOTEL_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(guestEmail));
            message.setSubject("✅ Booking Confirmation - " + HOTEL_NAME + " (Receipt: " + receiptNumber + ")");
            
            String emailContent = createEmailTemplate(
                guestName, receiptNumber, roomType, checkInDate, checkOutDate, 
                totalAmount, paymentMethod, roomCount, guestCount
            );
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            message.setSentDate(new Date());

            Transport.send(message);
            
            System.out.println("✅ Booking confirmation email sent successfully to: " + guestEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send booking confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean sendPasswordResetEmail(
            String guestEmail, 
            String guestName,
            String resetCode) {
        
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, HOTEL_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(guestEmail));
            message.setSubject("🔐 Password Reset Code - " + HOTEL_NAME);
            
            String emailContent = createPasswordResetEmailTemplate(guestName, resetCode);
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            message.setSentDate(new Date());

            Transport.send(message);
            
            System.out.println("✅ Password reset email sent successfully to: " + guestEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send password reset email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String createPasswordResetEmailTemplate(
            String guestName,
            String resetCode) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a");
        String currentDate = sdf.format(new Date());
        
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <style>" +
            "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }" +
            "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
            "        .header { background: linear-gradient(135deg, #4a1438 0%, #5a1848 100%); color: #ffffff; padding: 40px 30px; text-align: center; }" +
            "        .header h1 { margin: 0; font-size: 32px; font-weight: bold; }" +
            "        .header p { margin: 10px 0 0; font-size: 16px; opacity: 0.9; }" +
            "        .content { padding: 40px 30px; }" +
            "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }" +
            "        .message { font-size: 15px; color: #666; line-height: 1.6; margin-bottom: 30px; }" +
            "        .code-box { background: linear-gradient(135deg, #4a1438 0%, #5a1848 100%); border-radius: 10px; padding: 30px; margin: 30px 0; text-align: center; }" +
            "        .code-label { color: #e6b478; font-size: 14px; font-weight: 600; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; }" +
            "        .reset-code { font-size: 42px; color: #ffffff; font-weight: bold; letter-spacing: 8px; font-family: 'Courier New', monospace; margin: 15px 0; }" +
            "        .expiry-notice { color: #e6b478; font-size: 13px; margin-top: 15px; }" +
            "        .warning-box { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 20px; margin: 25px 0; border-radius: 5px; }" +
            "        .warning-box h3 { margin: 0 0 15px; color: #856404; font-size: 16px; }" +
            "        .warning-box ul { margin: 0; padding-left: 20px; color: #856404; line-height: 1.8; }" +
            "        .security-notice { background-color: #f8f9fa; border: 1px solid #dee2e6; border-radius: 5px; padding: 20px; margin: 25px 0; }" +
            "        .security-notice p { margin: 0; font-size: 14px; color: #666; line-height: 1.6; }" +
            "        .footer { background-color: #f9f9f9; padding: 30px; text-align: center; border-top: 1px solid #e0e0e0; }" +
            "        .footer p { margin: 8px 0; font-size: 13px; color: #777; }" +
            "        .contact-info { margin-top: 20px; padding-top: 20px; border-top: 1px solid #e0e0e0; }" +
            "        .contact-info p { margin: 5px 0; }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class='container'>" +
            "        <div class='header'>" +
            "            <h1>🔐 " + HOTEL_NAME + "</h1>" +
            "            <p>Password Reset Request</p>" +
            "        </div>" +
            "        " +
            "        <div class='content'>" +
            "            <p class='greeting'>Hello " + guestName + ",</p>" +
            "            " +
            "            <p class='message'>" +
            "                We received a request to reset your password for your " + HOTEL_NAME + " account. " +
            "                Use the code below to reset your password:" +
            "            </p>" +
            "            " +
            "            <div class='code-box'>" +
            "                <div class='code-label'>Your Reset Code</div>" +
            "                <div class='reset-code'>" + resetCode + "</div>" +
            "                <div class='expiry-notice'>⏱️ This code expires in 15 minutes</div>" +
            "            </div>" +
            "            " +
            "            <div class='warning-box'>" +
            "                <h3>⚠️ Important Security Information</h3>" +
            "                <ul>" +
            "                    <li>Enter this code in the password reset window</li>" +
            "                    <li>Do not share this code with anyone</li>" +
            "                    <li>This code can only be used once</li>" +
            "                    <li>If you didn't request this reset, please ignore this email</li>" +
            "                </ul>" +
            "            </div>" +
            "            " +
            "            <div class='security-notice'>" +
            "                <p><strong>🛡️ Didn't request a password reset?</strong></p>" +
            "                <p>If you did not request a password reset, please ignore this email. Your password will remain unchanged. " +
            "                For your security, we recommend changing your password regularly and using a strong, unique password.</p>" +
            "            </div>" +
            "            " +
            "            <p class='message'>" +
            "                If you have any questions or concerns about your account security, please don't hesitate to contact our support team." +
            "            </p>" +
            "            " +
            "            <p style='font-weight: 600; color: #4a1438; margin-top: 30px;'>" +
            "                Best regards,<br>" +
            "                The " + HOTEL_NAME + " Security Team" +
            "            </p>" +
            "        </div>" +
            "        " +
            "        <div class='footer'>" +
            "            <p style='font-size: 12px; color: #999;'>" +
            "                This password reset was requested on " + currentDate + "<br>" +
            "                Request originated from your account email address" +
            "            </p>" +
            "            " +
            "            <div class='contact-info'>" +
            "                <p><strong>" + HOTEL_NAME + "</strong></p>" +
            "                <p>📍 New Seaside Drive, Quezon City, Philippines</p>" +
            "                <p>📞 +639916649798</p>" +
            "                <p>✉️ info@undousehotel.com</p>" +
            "            </div>" +
            "            " +
            "            <p style='margin-top: 20px; font-size: 12px; color: #999;'>" +
            "                This is an automated security email. Please do not reply to this message.<br>" +
            "                For support inquiries, please contact us through our official channels above." +
            "            </p>" +
            "            " +
            "            <p style='margin-top: 15px; font-size: 11px; color: #aaa;'>" +
            "                © 2025 " + HOTEL_NAME + ". All rights reserved." +
            "            </p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";
    }
    
    private static String createEmailTemplate(
            String guestName,
            String receiptNumber,
            String roomType,
            String checkInDate,
            String checkOutDate,
            double totalAmount,
            String paymentMethod,
            int roomCount,
            int guestCount) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String currentDate = sdf.format(new Date());
        
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <style>" +
            "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }" +
            "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
            "        .header { background: linear-gradient(135deg, #4a1438 0%, #5a1848 100%); color: #ffffff; padding: 40px 30px; text-align: center; }" +
            "        .header h1 { margin: 0; font-size: 32px; font-weight: bold; }" +
            "        .header p { margin: 10px 0 0; font-size: 16px; opacity: 0.9; }" +
            "        .success-badge { background-color: #00c853; color: white; display: inline-block; padding: 8px 20px; border-radius: 20px; font-weight: bold; margin-top: 15px; font-size: 14px; }" +
            "        .content { padding: 40px 30px; }" +
            "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }" +
            "        .message { font-size: 15px; color: #666; line-height: 1.6; margin-bottom: 30px; }" +
            "        .details-box { background-color: #fff5f0; border-left: 4px solid #e6b478; padding: 25px; margin: 25px 0; border-radius: 5px; }" +
            "        .details-title { font-size: 18px; color: #4a1438; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #e6b478; padding-bottom: 10px; }" +
            "        .detail-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #f0f0f0; }" +
            "        .detail-row:last-child { border-bottom: none; }" +
            "        .detail-label { font-weight: 600; color: #555; font-size: 14px; }" +
            "        .detail-value { color: #333; font-size: 14px; text-align: right; }" +
            "        .receipt-number { background-color: #4a1438; color: white; padding: 15px; border-radius: 5px; text-align: center; margin: 20px 0; font-size: 16px; }" +
            "        .receipt-number strong { font-size: 18px; letter-spacing: 1px; }" +
            "        .total-amount { background-color: #e8f5e9; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0; }" +
            "        .total-amount .label { font-size: 14px; color: #666; margin-bottom: 5px; }" +
            "        .total-amount .amount { font-size: 32px; color: #00c853; font-weight: bold; }" +
            "        .important-info { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 20px; margin: 25px 0; border-radius: 5px; }" +
            "        .important-info h3 { margin: 0 0 15px; color: #856404; font-size: 16px; }" +
            "        .important-info ul { margin: 0; padding-left: 20px; color: #856404; line-height: 1.8; }" +
            "        .cta-button { display: inline-block; background-color: #4a1438; color: white; padding: 15px 40px; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 20px 0; text-align: center; }" +
            "        .cta-button:hover { background-color: #5a1848; }" +
            "        .footer { background-color: #f9f9f9; padding: 30px; text-align: center; border-top: 1px solid #e0e0e0; }" +
            "        .footer p { margin: 8px 0; font-size: 13px; color: #777; }" +
            "        .contact-info { margin-top: 20px; padding-top: 20px; border-top: 1px solid #e0e0e0; }" +
            "        .contact-info p { margin: 5px 0; }" +
            "        .social-links { margin-top: 15px; }" +
            "        .social-links a { display: inline-block; margin: 0 10px; color: #4a1438; text-decoration: none; font-size: 14px; }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class='container'>" +
            "        <div class='header'>" +
            "            <h1>" + HOTEL_NAME + "</h1>" +
            "            <p>Luxury Redefined</p>" +
            "            <div class='success-badge'>✅ BOOKING CONFIRMED</div>" +
            "        </div>" +
            "        " +
            "        <div class='content'>" +
            "            <p class='greeting'>Dear " + guestName + ",</p>" +
            "            " +
            "            <p class='message'>" +
            "                Thank you for choosing <strong>" + HOTEL_NAME + "</strong>! We are delighted to confirm your reservation. " +
            "                Your booking has been successfully processed and we look forward to welcoming you to our hotel." +
            "            </p>" +
            "            " +
            "            <div class='receipt-number'>" +
            "                📋 Receipt Number: <strong>" + receiptNumber + "</strong>" +
            "            </div>" +
            "            " +
            "            <div class='details-box'>" +
            "                <div class='details-title'>📝 Booking Details</div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Room Type:</span>" +
            "                    <span class='detail-value'><strong>" + roomType + "</strong></span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Number of Rooms:</span>" +
            "                    <span class='detail-value'>" + roomCount + " room(s)</span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Number of Guests:</span>" +
            "                    <span class='detail-value'>" + guestCount + " guest(s)</span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Check-in Date:</span>" +
            "                    <span class='detail-value'><strong>" + checkInDate + "</strong></span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Check-out Date:</span>" +
            "                    <span class='detail-value'><strong>" + checkOutDate + "</strong></span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Payment Method:</span>" +
            "                    <span class='detail-value'>" + paymentMethod + "</span>" +
            "                </div>" +
            "                " +
            "                <div class='detail-row'>" +
            "                    <span class='detail-label'>Booking Date:</span>" +
            "                    <span class='detail-value'>" + currentDate + "</span>" +
            "                </div>" +
            "            </div>" +
            "            " +
            "            <div class='total-amount'>" +
            "                <div class='label'>Total Amount Paid</div>" +
            "                <div class='amount'>₱" + String.format("%,.2f", totalAmount) + "</div>" +
            "            </div>" +
            "            " +
            "            <div class='important-info'>" +
            "                <h3>⚠️ Important Information</h3>" +
            "                <ul>" +
            "                    <li>Please present this confirmation email or your receipt number at check-in</li>" +
            "                    <li>Check-in time: 2:00 PM | Check-out time: 12:00 PM</li>" +
            "                    <li>Valid ID required for all guests</li>" +
            "                    <li>Cancellations must be made at least 3 days before check-in date</li>" +
            "                    <li>Full payment has been received and confirmed</li>" +
            "                </ul>" +
            "            </div>" +
            "            " +
            "            <p class='message'>" +
            "                If you have any questions or need to modify your reservation, please don't hesitate to contact us. " +
            "                We're here to ensure your stay is perfect!" +
            "            </p>" +
            "            " +
            "            <p class='message' style='margin-top: 30px;'>" +
            "                We look forward to serving you and making your stay memorable." +
            "            </p>" +
            "            " +
            "            <p style='font-weight: 600; color: #4a1438; margin-top: 30px;'>" +
            "                Warm regards,<br>" +
            "                The " + HOTEL_NAME + " Team" +
            "            </p>" +
            "        </div>" +
            "        " +
            "        <div class='footer'>" +
            "            <div class='contact-info'>" +
            "                <p><strong>" + HOTEL_NAME + "</strong></p>" +
            "                <p>📍 New Seaside Drive, Quezon City, Philippines</p>" +
            "                <p>📞 +639916649798</p>" +
            "                <p>✉️ info@undousehotel.com</p>" +
            "            </div>" +
            "            " +
            "            <p style='margin-top: 20px; font-size: 12px; color: #999;'>" +
            "                This is an automated confirmation email. Please do not reply to this message.<br>" +
            "                For inquiries, please contact us through our official channels above." +
            "            </p>" +
            "            " +
            "            <p style='margin-top: 15px; font-size: 11px; color: #aaa;'>" +
            "                © 2025 " + HOTEL_NAME + ". All rights reserved." +
            "            </p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";
    }
    
    public static boolean testEmailConfiguration() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });
            
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.close();
            
            System.out.println("✅ Email configuration is valid!");
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Email configuration error: " + e.getMessage());
            return false;
        }
    }
}