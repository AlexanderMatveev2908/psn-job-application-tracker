package server.models.user.etc;
// package server.models.user.side;

// import java.util.List;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.PropertyNamingStrategies;
// import com.fasterxml.jackson.databind.SerializationFeature;

// import server.decorators.flow.ErrAPI;
// import server.models.token.MyToken;
// import server.models.user.User;

// public class UserPopulated extends User {

// private String tokens;

// private static final ObjectMapper JACK = new ObjectMapper()
// .enable(SerializationFeature.INDENT_OUTPUT)
// .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

// public UserPopulated() {
// }

// @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch",
// "CallToPrintStackTrace" })
// public List<MyToken> getTokens() {
// try {
// return JACK.readValue(tokens, new TypeReference<List<MyToken>>() {
// });
// } catch (Exception err) {
// throw new ErrAPI("fail parser tokens", 500);
// }
// }

// public void settokens(String tokens) {
// this.tokens = tokens;
// }

// }
