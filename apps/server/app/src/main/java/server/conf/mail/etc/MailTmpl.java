package server.conf.mail.etc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.paths.Hiker;

@Service @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class MailTmpl {

    private final EnvKeeper envKeeper;

    public String readTmpl() {
        try {
            if (Files.exists(Hiker.MAIL_TMPL))
                return Files.readString(Hiker.MAIL_TMPL, StandardCharsets.UTF_8);

            try (InputStream in = getClass().getClassLoader().getResourceAsStream("mail_templates/template.html")) {
                if (in == null)
                    throw new ErrAPI("missing mail template in classpath");

                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException err) {
            throw new ErrAPI(err.getMessage());
        }
    }

    public void checkNew(String content) {
        try (BufferedWriter bw = Files.newBufferedWriter(Hiker.SERVER_DIR.resolve("dummy.html"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(content);
            bw.newLine();
        } catch (IOException err) {
            throw new ErrAPI("err writing parsed file");
        }
    }

    public String replacePlaceholder(String firstName, String clientToken) {
        String txt = readTmpl();

        StringBuffer buff = new StringBuffer();

        Pattern reg = Pattern.compile("\\$\\{(.*?)}");
        Matcher match = reg.matcher(txt);

        Map<String, String> vals = Map.of("firstName", firstName, "url",
                envKeeper.getFrontUrl() + "/verify?cbcHmacToken=" + clientToken);

        while (match.find()) {
            String key = match.group(1);
            String repl = vals.getOrDefault(key, "👻");
            match.appendReplacement(buff, Matcher.quoteReplacement(repl));
        }

        match.appendTail(buff);

        return buff.toString();
    }
}
