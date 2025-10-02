package server.conf;

import java.util.regex.Pattern;
import server.decorators.flow.ErrAPI;

public final class Reg {
        public static final String NAME = "^[\\p{L}\\s,`'\\-]*$";
        public static final String JOB_NAME = "^[\\p{L}\\s,`'/\\-]*$";
        public static final String PWD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])\\S{8,}$";
        public static final String TXT = "^[\\p{L}\\d\\s\\-'\\\".,;!?]*$";
        public static final String INT = "^\\d+$";
        public static final String FLOAT = "^(?:\\d+(?:\\.\\d{1,2})?|\\.\\d{1,2})$";
        public static final String UUID = "^([a-f0-9]{8})-([a-f0-9]{4})-4[a-f0-9]{3}-([a-f0-9]{4})-([a-f0-9]{12})$";
        public static final String JWT = "^(?:[A-Za-z0-9_-]+={0,2}\\.){2}[A-Za-z0-9_-]+={0,2}$";
        public static final String JWE = "^(?:[A-Za-z0-9_-]+={0,2}\\.){4}[A-Za-z0-9_-]+={0,2}$";
        public static final String CBC_HMAC = "^(?:[A-Fa-f0-9]+\\.){3}[A-Fa-f0-9]+$";
        public static final String TOTP_SECRET = "^[A-Z2-7]{32}$";
        public static final String TOTP_CODE = "^\\d{6}$";
        public static final String BKP_CODE = "^[A-F0-9]{4}-[A-F0-9]{4}$";
        public static final String BASE_64 = "^[A-Za-z0-9+/]*={0,2}$";
        public static final String DATE_PICKER = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

        private Reg() {
                throw new ErrAPI("Keep Reg class as static helper", 500);
        }

        public static boolean checkReg(String arg, String reg) {
                return arg != null && Pattern.matches(reg, arg);
        }

        public static boolean isName(String arg) {
                return checkReg(arg, NAME);
        }

        public static boolean isJobName(String arg) {
                return checkReg(arg, JOB_NAME);
        }

        public static boolean isPwd(String arg) {
                return checkReg(arg, PWD);
        }

        public static boolean isTxt(String arg) {
                return checkReg(arg, TXT);
        }

        public static boolean isInt(String arg) {
                return checkReg(arg, INT);
        }

        public static boolean isFloat(String arg) {
                return checkReg(arg, FLOAT);
        }

        public static boolean isUUID(String arg) {
                return checkReg(arg, UUID);
        }

        public static boolean isJWT(String arg) {
                return checkReg(arg, JWT);
        }

        public static boolean isJWE(String arg) {
                return checkReg(arg, JWE);
        }

        public static boolean isCbcHmac(String arg) {
                return checkReg(arg, CBC_HMAC);
        }

        public static boolean isTotpSecret(String arg) {
                return checkReg(arg, TOTP_SECRET);
        }

        public static boolean isTotpCode(String arg) {
                return checkReg(arg, TOTP_CODE);
        }

        public static boolean isBkpCode(String arg) {
                return checkReg(arg, BKP_CODE);
        }

        public static boolean isDatePicker(String arg) {
                return checkReg(arg, DATE_PICKER);
        }

        public static boolean isB64(String arg) {
                return checkReg(arg, BASE_64);
        }
}
