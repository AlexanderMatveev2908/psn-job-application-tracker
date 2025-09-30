package server.conf.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;
import server.conf.mail.etc.MailTmpl;
import server.conf.mail.etc.SubjEmailT;
import server.decorators.flow.ErrAPI;
import server.models.user.User;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class MailSvc {

    private final JavaMailSender mailSender;
    private final EnvKeeper envKeeper;
    private final MailTmpl mailTmpl;

    public void sendTxtMail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(envKeeper.getNextPblSmptFrom());
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        mailSender.send(msg);
    }

    public void sendHtmlMail(SubjEmailT subject, User user) {
        MimeMessage msg = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setFrom(envKeeper.getNextPblSmptFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(subject.getValue());
            helper.setText(mailTmpl.replacePlaceholder(user.getFirstName()), true);

            if (!envKeeper.getEnvMode().equals(EnvMode.TEST))
                mailSender.send(msg);

        } catch (Exception err) {
            throw new ErrAPI(err.getMessage());
        }
    }

    public Mono<Void> sendRctTxtMail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> sendTxtMail(to, subject, text))
                .subscribeOn(Schedulers.boundedElastic()).doOnSuccess((nl) -> {
                    System.out.println("📫 mail sent");
                }).onErrorResume((err) -> {
                    System.out.println("❌ err sending mail");

                    return Mono.empty();
                }).then();
    }

    public Mono<Void> sendRctHtmlMail(SubjEmailT subject, User user) {
        return Mono.fromRunnable(() -> sendHtmlMail(subject, user)).doOnSuccess((nl) -> {
            System.out.println("📫 mail sent");
        }).onErrorResume((err) -> {
            System.out.println("❌ err sending mail");
            return Mono.empty();
        }).then();
    }

}
