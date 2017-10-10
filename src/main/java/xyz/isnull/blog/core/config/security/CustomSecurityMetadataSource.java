package xyz.isnull.blog.core.config.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

    private static final Logger logger = Logger.getLogger(CustomSecurityMetadataSource.class);

    private Map<String, Collection<ConfigAttribute>> resouceMap = null;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private String urlroles;

    public CustomSecurityMetadataSource(String urlroles){
        super();
        this.urlroles = urlroles;
        resouceMap = loadResourceMatchAuthority();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String url = ((FilterInvocation) o).getRequestUrl();
        logger.info("request url is  " + url);

        if(resouceMap == null){
            resouceMap = loadResourceMatchAuthority();
        }

        Iterator<String> iterator = resouceMap.keySet().iterator();
        while (iterator.hasNext()){
            String url_ = iterator.next();
            if(pathMatcher.match(url_,url)){
                return resouceMap.get(url_);
            }
        }
        return resouceMap.get(url);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private Map<String, Collection<ConfigAttribute>> loadResourceMatchAuthority(){
        //这里的主要目的是得到所有的 url= roles  即是路径：权限列表。
        Map<String, Collection<ConfigAttribute>> map = new HashMap<String, Collection<ConfigAttribute>>();
        if(urlroles != null && !urlroles.isEmpty()){
            String[] resouces = urlroles.split(";");
            for (String source : resouces){
                String[] urls = source.split("=");
                String[] roles = urls[1].split(",");
                Collection<ConfigAttribute> configList = new ArrayList<ConfigAttribute>();
                for(String role : roles){
                    ConfigAttribute config = new SecurityConfig(role.trim());
                    configList.add(config);
                }
                map.put(urls[0].trim(), configList);
            }
        } else {
            logger.error("'securityconfig.urlroles' must be set");
        }
        logger.info("Loaded UrlRoles Resources.");
        return map;
    }

}
