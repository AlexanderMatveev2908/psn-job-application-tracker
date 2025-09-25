package server.conf.mail.etc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class MailTmpl {

    private final EnvKeeper envKeeper;

    public String readTmpl() {
        try (BufferedReader br = Files.newBufferedReader(Hiker.MAIL_TMPL, StandardCharsets.UTF_8)) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null)
                content.append(line);

            return content.toString();
        } catch (IOException err) {
            throw new ErrAPI("err reading mail tmpl", 500);
        }
    }

    public void checkNew(String content) {
        try (BufferedWriter bw = Files.newBufferedWriter(Hiker.SERVER_DIR.resolve("dummy.html"), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(content);
            bw.newLine();
        } catch (IOException err) {
            throw new ErrAPI("err writing parsed file", 500);
        }
    }

    public String replacePlaceholder(String firstName) {
        String txt = readTmpl();

        StringBuffer buff = new StringBuffer();

        Pattern reg = Pattern.compile("\\$\\{(.*?)}");
        Matcher match = reg.matcher(txt);

        Map<String, String> vals = Map.of("firstName", firstName, "url", envKeeper.getFrontUrl() + "/verify");

        while (match.find()) {
            String key = match.group(1);
            String repl = vals.getOrDefault(key, "👻");
            match.appendReplacement(buff, Matcher.quoteReplacement(repl));
        }

        match.appendTail(buff);

        return buff.toString();
    }
}
