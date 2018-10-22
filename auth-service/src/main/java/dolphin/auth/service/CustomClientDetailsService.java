package dolphin.auth.service;

import com.google.common.collect.Sets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * Created by lyl on 2017/1/19.
 */
@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private Map<String,ClientDetails> detailsMap=new HashMap();
    public CustomClientDetailsService() throws Exception{
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource resource=patternResolver.getResource("classpath:client.yml");
        Yaml yaml = new Yaml();
        Map<String, Object> map=(Map<String,Object>)yaml.load(resource.getInputStream());
        List<Map<String, Object>> infos=(List<Map<String,Object>>)map.get("clients");
        for(Map<String,Object> infoMap:infos){
            CustomClientDetails customClientDetails=new CustomClientDetails(infoMap);
            detailsMap.put(customClientDetails.getClientId(), customClientDetails);
        }

    }
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return detailsMap.get(clientId);
    }

    private class CustomClientDetails implements ClientDetails{
        private Map<String,Object> info;
        public CustomClientDetails(Map<String,Object> map){
            info=map;
        }
        @Override
        public String getClientId() {
            return (String)info.get("clientId");
        }

        @Override
        public Set<String> getResourceIds() {
            return transList((List<String>)info.get("resourceIds"));

        }

        @Override
        public boolean isSecretRequired() {
            Boolean b=(Boolean)info.get("isSecretRequired");
            return b==null?false:b.booleanValue();
        }

        @Override
        public String getClientSecret() {
            return (String)info.get("clientSecret");
        }

        @Override
        public boolean isScoped() {
            Boolean b=(Boolean)info.get("isScoped");
            return b==null?false:b.booleanValue();
        }

        @Override
        public Set<String> getScope() {
            return transList((List<String>)info.get("scope"));
        }

        @Override
        public Set<String> getAuthorizedGrantTypes() {
            return transList((List<String>)info.get("authorizedGrantTypes"));
        }

        @Override
        public Set<String> getRegisteredRedirectUri() {
            return transList((List<String>)info.get("registeredRedirectUri"));
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            List<String> list=(List<String>)info.get("authorities");
            if(list!=null){
                List<GrantedAuthority> ret=new ArrayList<>();
               for(String s:list){
                   ret.add(new SimpleGrantedAuthority(s));
               }
               return ret;
            }
            return null;
        }

        @Override
        public Integer getAccessTokenValiditySeconds() {
            return (Integer) info.get("accessTokenValiditySeconds");
        }

        @Override
        public Integer getRefreshTokenValiditySeconds() {
            return (Integer) info.get("refreshTokenValiditySeconds");
        }

        @Override
        public boolean isAutoApprove(String scope) {
            Boolean b=(Boolean)info.get("isAutoApprove");
            return b==null?false:b.booleanValue();
        }

        @Override
        public Map<String, Object> getAdditionalInformation() {
            return (Map<String,Object>)info.get("additionalInformation");
        }

        private Set<String> transList(List<String> list){
            return list==null?null:Sets.newHashSet(list);
        }
    }
}
