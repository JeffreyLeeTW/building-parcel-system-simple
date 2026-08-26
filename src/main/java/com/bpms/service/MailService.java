package com.bpms.service;

import com.bpms.entity.Parcel;
import com.bpms.entity.PickupRecord;
import com.bpms.entity.Resident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private final JavaMailSender mailSender;
    private final String baseUrl;
    private final String fromAddress;

    public MailService(JavaMailSender mailSender,
                        @Value("${app.base-url}") String baseUrl,
                        @Value("${spring.mail.username:}") String fromAddress) {
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
        this.fromAddress = fromAddress;
    }

    public void sendArrivalNotification(Resident resident, Parcel parcel) {
        String agentLink = baseUrl + "/agent-authorize?token=" + parcel.getAgentToken();
        String body = "%s 您好，\n\n您有一件包裹已送達管理室，資訊如下：\n\n包裹碼：%s\n櫃位：%s\n到貨時間：%s\n\n請攜帶實體身分證件至管理室，向管理員出示「4 位數包裹碼」即可領取。\n\n若您無法親自領取，可點擊以下連結，線上設定代領人：\n%s\n\n（此為系統自動發送信件，請勿直接回覆）"
                .formatted(resident.getName(), parcel.getParcelCode(), parcel.getCabinetLabel(),
                        parcel.getArrivalTime().format(FORMAT), agentLink);
        send(resident.getEmail(), "【大樓包裹管理平台】包裹到貨通知", body);
    }

    public void sendPickupConfirmation(Resident resident, Parcel parcel, PickupRecord record) {
        String body = "%s 您好，\n\n您的包裹已完成領取簽收，資訊如下：\n\n包裹編號：%s\n領取方式：%s\n實際領取人：%s\n領取時間：%s\n經辦管理員：%s\n\n若非您本人或授權代領人領取，請盡速聯繫大樓管理室。\n\n（此為系統自動發送信件，請勿直接回覆）"
                .formatted(resident.getName(), parcel.getDisplayCode(),
                        parcel.isProxy() ? "授權代領" : "本人領取",
                        record.getActualPickerName(),
                        record.getPickupTime().format(FORMAT),
                        record.getHandlingParcelman().getName());
        send(resident.getEmail(), "【大樓包裹管理平台】包裹領取簽收確認", body);
    }

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (fromAddress != null && !fromAddress.isBlank()) {
                message.setFrom(fromAddress);
            }
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("寄送 Email 失敗，收件者：{}，主旨：{}", to, subject, e);
        }
    }
}
