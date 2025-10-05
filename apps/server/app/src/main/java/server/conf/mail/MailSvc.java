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
import server.decorators.flow.ErrAPI;
import server.lib.dev.MyLog;
import server.models.token.etc.TokenT;
import server.models.user.User;

@Service @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
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

    public void sendHtmlMail(TokenT tokenT, User user, String clientToken) {
        MimeMessage msg = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setFrom(envKeeper.getNextPblSmptFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(tokenT.getSubject());
            helper.setText(mailTmpl.replacePlaceholder(user.getFirstName(), clientToken), true);

            if (!envKeeper.getEnvMode().equals(EnvMode.TEST))
                mailSender.send(msg);

        } catch (Exception err) {
            throw new ErrAPI(err.getMessage());
        }
    }

    public Mono<Void> sendRctTxtMail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> sendTxtMail(to, subject, text)).subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess((nl) -> {
                    MyLog.log("📫 mail sent");
                }).onErrorResume((err) -> {
                    MyLog.logErr(err);
                    return Mono.empty();
                }).then();
    }

    public Mono<Void> sendRctHtmlMail(TokenT tokenT, User user, String clientToken) {
        return Mono.fromRunnable(() -> sendHtmlMail(tokenT, user, clientToken)).doOnSuccess((nl) -> {
            MyLog.log("📫 mail sent");
        }).onErrorResume((err) -> {

            MyLog.logErr(err);

            return Mono.error(new ErrAPI("err sending mail"));
        }).then();
    }

}
