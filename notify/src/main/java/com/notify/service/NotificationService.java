package com.notify.service;
import com.notify.bean.Notify;
import com.notify.repo.NotificationRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Value("${spring.mail.username}")
  private String fromEmail;
  private final NotificationRepo notificationRepo;
  private final JavaMailSender mailSender;

  public NotificationService(NotificationRepo notificationRepo, JavaMailSender mailSender) {
    this.notificationRepo = notificationRepo;
    this.mailSender = mailSender;
  }

  public List<Notify> getAllNotify() {
    return notificationRepo.findAll();
  }

  public Notify createNotify(Notify notify) {

    return notificationRepo.save(notify);
  }

  public void deleteNotify(Long id) {
     notificationRepo.deleteById(id);
  }

  public void sendNotification(String recipient, String message) {

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(fromEmail);
    mailMessage.setTo("waghashish007@gmail.com");
    mailMessage.setSubject("Notification");
    mailMessage.setText(message);

    // Send the email
    mailSender.send(mailMessage);

    System.out.println("Notification email sent to " + recipient + ": " + message);

  }
}
