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

            List<String> msgs = errs.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            return Mono.error(new ErrAPI(msgs.get(0), 400, Map.entry("errors", msgs)));

        })).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400))).doOnError(err -> {
            System.out.println(err.getMessage());
        });
    }

}