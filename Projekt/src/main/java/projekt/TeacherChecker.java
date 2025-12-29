package projekt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TeacherChecker {

    public static Set<String> loadTeachersFromKatedra() {
        String url = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";
        Set<String> names = new HashSet<>();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new URI(url).toURL().openStream(), StandardCharsets.UTF_8))) {

            String html = in.lines().collect(Collectors.joining("\n"));

            Pattern pattern = Pattern.compile("([A-ZÁČĎÉĚÍŇÓŘŠŤÚŮÝŽ][^<]+?)(?:</a>)");
            Matcher matcher = pattern.matcher(html);

            while (matcher.find()) {
                String raw = matcher.group(1).trim();
                raw = raw.replaceAll("<[^>]+>", "");
                if (raw.split("\\s+").length >= 2) names.add(raw);
            }

        } catch (Exception e) {
            System.err.println("Chyba: Nelze nacist ucitele");
        }

        return names;
    }

    public static boolean isTeacher(String userName, Set<String> teachers) {
        for (String t : teachers) {
            if (t.toLowerCase().contains(userName.toLowerCase()) ||
                    userName.toLowerCase().contains(t.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
