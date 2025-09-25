package server.conf.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import server.conf.env_conf.EnvKeeper;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class MailSvc {

    private final JavaMailSender mailSender;
    private final EnvKeeper envKeeper;

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(envKeeper.get("nextPblSmptFrom"));
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        mailSender.send(msg);
    }

    public Mono<Void> sendRctMail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> sendMail(to, subject, text))
                .subscribeOn(Schedulers.boundedElastic()).doOnSuccess((nl) -> {
                    System.out.println("📫 mail sent");
                }).onErrorResume((err) -> {
                    System.out.println("❌ err sending mail");

                    return Mono.empty();
                }).then();
    }

}
