import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final String absenderEmail;
    private final String appPasswort;

    public EmailService(String absenderEmail, String appPasswort) {
        this.absenderEmail = absenderEmail;
        this.appPasswort = appPasswort;
    }

    public void emailSenden(String empfaenger, String betreff, String nachricht) 
            throws MessagingException {
        
        // SMTP-Konfiguration fuer Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Sitzung mit Authentifizierung
        Session sitzung = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(absenderEmail, appPasswort);
            }
        });
        // E-Mail zusammenbauen
        Message email = new MimeMessage(sitzung);
        email.setFrom(new InternetAddress(absenderEmail));
        email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaenger));
        email.setSubject(betreff);
        email.setText(nachricht);
        // Senden
        Transport.send(email);
    }
}