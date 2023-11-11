package solution.com.lattmat.security.oauth2User;

import solution.com.lattmat.security.enumeration.LoginProvider;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public abstract String getId();
    public abstract String getFirstName();
    public abstract String getLastName();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getPicture();
    public abstract LoginProvider getLoginProvider();
}
