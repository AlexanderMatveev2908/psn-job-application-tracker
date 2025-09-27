package server._lib_tests;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.datafaker.Faker;

import lombok.Data;

@Data
public class MyPayloads {
    private final Faker faker = new Faker();

    public Map<String, Object> register() {
        return new HashMap<>(Map.of(
                "firstName", faker.name().firstName(),
                "lastName", faker.name().lastName(),
                "email", faker.internet().emailAddress(),
                "password", "8cLS4XY!{2Wdvl4*l^4",
                "confirmPassword", "8cLS4XY!{2Wdvl4*l^4",
                "terms", "true"));
    }

    public Map<String, Object> registerPatch(String key, Object newValue) {
        return changeKeyForm(register(), el -> el.getKey().equals(key) ? newValue : el.getValue());

    }

    public Map<String, Object> changeKeyForm(Map<String, Object> form, Function<Map.Entry<String, Object>, Object> cb) {
        return form.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, el -> cb.apply(el)));
    }

}
