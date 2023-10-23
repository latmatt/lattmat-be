package solution.com.lattmat.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils {

    public static HttpSession getCurrentSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }

    public static Object getAttribute(String name){
        return getCurrentSession().getAttribute(name);
    }
}
