package server._lib_tests;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.datafaker.Faker;

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
        return form.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, el -> cb.apply(el)));
    }

    public static Map<String, Object> changeValByKey(Map<String, Object> form, String key, Object newVal) {
        return changeValByKey(form, el -> el.getKey().equals(key) ? newVal : el.getValue());
    }

}
