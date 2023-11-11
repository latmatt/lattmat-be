package solution.com.lattmat.security.enumeration;

public enum LoginProvider {

    APP("APP"), FACEBOOK("FACEBOOK"), GOOGLE("GOOGLE");

    private String label;

    LoginProvider(String name) {
        label = name;
    }

    public String getLabel() {
        return label;
    }
}
