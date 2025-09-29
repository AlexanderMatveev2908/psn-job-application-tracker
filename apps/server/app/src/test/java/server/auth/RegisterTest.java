package server.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import server._lib_tests.MyAssrt;
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.conf.Reg;

@SpringBootTest
@AutoConfigureWebTestClient
public class RegisterTest {

        private final static MyPayloads payloads = new MyPayloads();
        private final static String URL = "/api/v1/auth/register";

        @Autowired
        private WebTestClient web;

        private ReqT req;

        @BeforeEach
        void setup() {
                req = ReqT.withUrl(web, URL);
        }

        static Stream<Arguments> okCases() {
                return Stream.of(
                                Arguments.of("user created", 201, payloads.register()));
        }

        @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
        @ParameterizedTest
        @MethodSource("okCases")
        void ok(String msg, int status, Object bd) {
                ResT res = req.method(HttpMethod.POST).body(bd).send();

                MyAssrt.assrt(res, msg, status);

                Map<String, Object> user = (Map<String, Object>) res.getBd()
                                .getOrDefault("user", Map.of());

                String id = (String) user.get("id");

                assertTrue(Reg.isUUID(id), MyAssrt.buildStr("valid UUID", id));

        }

        static Stream<Arguments> errCases() {
                return Stream.of(
                                Arguments.of("data not provided", 400, null),
                                Arguments.of("wrong data format", 400, "server do not expect a string as body"),
                                Arguments.of("first name invalid", 422,
                                                payloads.registerPatch("firstName",
                                                                "<script>alert(\"hacked😈\")</script>")),
                                Arguments.of("last name required", 422,
                                                payloads.registerPatch("lastName", "")),
                                Arguments.of("email invalid", 422,
                                                payloads.registerPatch("email", "@@@invalid....email?")),
                                Arguments.of("password invalid", 422,
                                                payloads.changeValByKey(payloads.registerPatch("password", "123"),
                                                                "confirmPassword", "123")),
                                Arguments.of("passwords do not match", 422,
                                                payloads.registerPatch("confirmPassword", "different from password")),
                                Arguments.of("an account with this email already exists", 409, payloads.register()));
        }

        @ParameterizedTest
        @MethodSource("errCases")
        void err(String msg, int status, Object bd) {
                ResT res = req.method(HttpMethod.POST).body(bd).send();

                if (!msg.contains("already exists")) {
                        MyAssrt.assrt(res, msg, status);
                        return;
                }

                ResT secondCall = req.method(HttpMethod.POST).body(bd).send();
                MyAssrt.assrt(secondCall, msg, status);

        }
}
