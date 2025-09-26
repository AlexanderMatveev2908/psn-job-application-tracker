package server.features.auth.middleware.register;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.features.auth.paperwork.RegisterForm;
import server.middleware.BaseMdw;
import server.middleware.security.RateLimit;

@Component
@RequiredArgsConstructor
public class RegisterMdw extends BaseMdw {
    private final RateLimit rl;
    private final Validator checker;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {

        if (!api.isSamePath("/api/v1/auth/register"))
            return chain.filter(api);

        return rl.limit(api).then(api.getBd(new TypeReference<Map<String, String>>() {

        }).flatMap(bd -> {
            RegisterForm form = RegisterForm.mapToForm(bd);

            Set<ConstraintViolation<RegisterForm>> errs = checker.validate(form);

            if (errs.isEmpty())
                return chain.filter(api);

            List<Map<String, String>> errors = errs.stream()
                    .map(err -> Map.of(
                            "field", err.getPropertyPath().toString(),
                            "msg", err.getMessage()))
                    .toList();

            return Mono.error(new ErrAPI(errors.get(0).get("msg"), 400, Map.entry("errs", errors)));

        })).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

}