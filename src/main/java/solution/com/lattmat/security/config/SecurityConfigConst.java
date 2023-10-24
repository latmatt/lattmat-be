package solution.com.lattmat.security.config;

public class SecurityConfigConst {
    public static final String SECRET_KEY = "16502ee43b73ce88149ca42ab31db111063820bd106505a651f345ecbb33a7bbe6b38578b23baee473c7aaf89c8e490c8e1429fbb264f137d1e2b3fb2ef396d8";
    public static final Long EXPIRATION_MILLS = 300000L;
    public static final String JWT_COOKIE = "lattmat-jwt";
    public static final String REFRESH_TOKEN_COOKIE = "lattmat-jwt-refresh";
    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
}
