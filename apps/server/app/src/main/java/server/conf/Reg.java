package server.conf;

import java.util.regex.Pattern;

import server.decorators.flow.ErrAPI;

public final class Reg {

        public static final Pattern REG_NAME = Pattern.compile("^[\\p{L}\\s,`'\\-]*$");

        public static final Pattern REG_JOB_NAME = Pattern.compile("^[\\p{L}\\s,`'/\\-]*$");

        public static final Pattern REG_PWD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])\\S{8,}$");

        public static final Pattern REG_TXT = Pattern.compile("^[\\p{L}\\d\\s\\-'\\" + '"' + "\\.,;!?]*$");

        public static final Pattern REG_INT = Pattern.compile("^\\d+$");

        public static final Pattern REG_FLOAT = Pattern.compile("^(?:\\d+(?:\\.\\d{1,2})?|\\.\\d{1,2})$");

        public static final Pattern REG_UUID = Pattern
                        .compile("^([a-f0-9]{8})-([a-f0-9]{4})-4[a-f0-9]{3}-([a-f0-9]{4})-([a-f0-9]{12})$");

        public static final Pattern REG_JWT = Pattern
                        .compile("^(?=.{171}$)[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$");

        public static final Pattern REG_JWE = Pattern.compile("^[A-Fa-f0-9]{1004}$");

        public static final Pattern REG_CBC_HMAC = Pattern
                        .compile("^(?=.{600,}$)[A-Fa-f0-9]{400,}\\.[A-Fa-f0-9]{32}\\.[A-Fa-f0-9]{128}\\.[A-Fa-f0-9]{64}$");

        public static final Pattern REG_TOTP_SECRET = Pattern.compile("^[A-Z2-7]{32}$");

        public static final Pattern REG_TOTP_CODE = Pattern.compile("^\\d{6}$");

        public static final Pattern REG_BKP_CODE = Pattern.compile("^[A-F0-9]{4}-[A-F0-9]{4}$");

        public static final Pattern REG_DATE_PICKER = Pattern
                        .compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");

        private Reg() {
                throw new ErrAPI("keep Reg cls as static helper");
        }

        public static boolean checkReg(String arg, Pattern reg) {
                return arg != null && reg.matcher(arg).matches();
        }

        public static boolean isName(String arg) {
                return arg != null && REG_NAME.matcher(arg).matches();
        }

        public boolean isJobName(String arg) {
                return arg != null && REG_JOB_NAME.matcher(arg).matches();
        }

        public static boolean isPwd(String arg) {
                return arg != null && REG_PWD.matcher(arg).matches();
        }

        public static boolean isTxt(String arg) {
                return arg != null && REG_TXT.matcher(arg).matches();
        }

        public static boolean isInt(String arg) {
                return arg != null && REG_INT.matcher(arg).matches();
        }

        public static boolean isFloat(String arg) {
                return arg != null && REG_FLOAT.matcher(arg).matches();
        }

        public static boolean isUUID(String arg) {
                return arg != null && REG_UUID.matcher(arg).matches();
        }

        public static boolean isJWT(String arg) {
                return arg != null && REG_JWT.matcher(arg).matches();
        }

        public static boolean isJWE(String arg) {
                return arg != null && REG_JWE.matcher(arg).matches();
        }

        public static boolean isCbcHmac(String arg) {
                return arg != null && REG_CBC_HMAC.matcher(arg).matches();
        }

        public static boolean isTotpSecret(String arg) {
                return arg != null && REG_TOTP_SECRET.matcher(arg).matches();
        }

        public static boolean isTotpCode(String arg) {
                return arg != null && REG_TOTP_CODE.matcher(arg).matches();
        }

        public static boolean isBkpCode(String arg) {
                return arg != null && REG_BKP_CODE.matcher(arg).matches();
        }

        public static boolean isDatePicker(String arg) {
                return arg != null && REG_DATE_PICKER.matcher(arg).matches();
        }

}
