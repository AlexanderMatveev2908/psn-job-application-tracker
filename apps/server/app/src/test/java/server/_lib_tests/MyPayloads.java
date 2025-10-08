package server._lib_tests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import net.datafaker.Faker;
import server.models.applications.etc.JobApplStatusT;

public final class MyPayloads {
    private final static Faker faker = new Faker();

    public static Map<String, Object> register() {
        return new HashMap<>(Map.of("firstName", faker.name().firstName(), "lastName", faker.name().lastName(), "email",
                faker.internet().emailAddress(), "password", "8cLS4XY!{2Wdvl4*l^4", "confirmPassword",
                "8cLS4XY!{2Wdvl4*l^4", "terms", "true"));
    }

    public static Map<String, Object> login() {
        return new HashMap<>(Map.of("email", faker.internet().emailAddress(), "password", "8cLS4XY!{2Wdvl4*l^4"));
    }

    public static <T> T pick(T[] choices) {
        if (choices == null || choices.length == 0)
            return null;

        int idx = ThreadLocalRandom.current().nextInt(choices.length);
        return choices[idx];
    }

    public static String randomDate() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);

        long randomDay = ThreadLocalRandom.current().nextLong(start.toEpochDay(), end.toEpochDay() + 1);

        return LocalDate.ofEpochDay(randomDay).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static Map<String, Object> application() {
        return new HashMap<>(Map.of("companyName", faker.company().name(), "positionName", faker.job().title(),
                "status", pick(JobApplStatusT.values()).getValue(), "appliedAt", randomDate(), "notes",
                faker.lorem().sentence()));
    }

    public static Map<String, Object> applicationPatch(String key, Object newValue) {
        return changeValByKey(application(), el -> el.getKey().equals(key) ? newValue : el.getValue());
    }

    public static Map<String, Object> extractLoginForm(Map<String, Object> registerForm) {
        return Map.of("email", registerForm.get("email"), "password", registerForm.get("password"));
    }

    public static Map<String, Object> loginPatch(Map<String, Object> registerForm, String key, String newVal) {
        return changeValByKey(extractLoginForm(registerForm), key, newVal);
    }

    public static Map<String, Object> registerPatch(String key, Object newValue) {
        return changeValByKey(register(), el -> el.getKey().equals(key) ? newValue : el.getValue());
    }

    public static Map<String, Object> changeValByKey(Map<String, Object> form,
            Function<Map.Entry<String, Object>, Object> cb) {
        Map<String, Object> result = new HashMap<>();

        for (var pair : form.entrySet())
            result.put(pair.getKey(), cb.apply(pair));

        return result;
    }

    public static Map<String, Object> changeValByKey(Map<String, Object> form, String key, Object newVal) {
        return changeValByKey(form, el -> el.getKey().equals(key) ? newVal : el.getValue());
    }

}
