package server.middleware.form_checkers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;

@Service
@RequiredArgsConstructor
public class FormChecker {
    private final Validator checker;

    public <T> Mono<Void> checkBdForm(Api api, T form) {
        Set<ConstraintViolation<T>> errs = checker.validate(form);

        if (errs.isEmpty())
            return Mono.fromRunnable(() -> api.setAttr("mappedData", form));

        List<Map<String, String>> errors = errs.stream()
                .map(err -> Map.of(
                        "field", err.getPropertyPath().toString(),
                        "msg", err.getMessage()))
                .toList();

        return Mono.error(new ErrAPI(errors.get(0).get("msg"), 400, Map.of("errs", errors)));
    }
}
