package server.conf.mail.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import server.decorators.flow.ErrAPI;
import server.lib.paths.Hiker;

public class MailTmpl {
    public String readTmpl() {
        try (BufferedReader br = Files.newBufferedReader(Hiker.MAIL_TMPL, StandardCharsets.UTF_8)) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                content.append(line);
            }

            return content.toString();
        } catch (IOException err) {
            throw new ErrAPI("err reading mail tmpl", 500);
        }
    }
}
