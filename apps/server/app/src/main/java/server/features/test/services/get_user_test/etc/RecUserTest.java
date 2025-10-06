package server.features.test.services.get_user_test.etc;

import reactor.util.function.Tuple2;
import server.lib.security.tfa.etc.Rec2FA;
import server.models.user.User;

public record RecUserTest(User user, String plainPwd, Rec2FA rec2FA) {

  public static RecUserTest fromTpl(Tuple2<User, String> tpl) {
    return new RecUserTest(tpl.getT1(), tpl.getT2(), null);
  }

}
