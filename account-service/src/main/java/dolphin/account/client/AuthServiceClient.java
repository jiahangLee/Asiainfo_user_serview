package dolphin.account.client;


import dolphin.account.domain.AuthUser;
import dolphin.account.domain.ChangePasswordEntity;
import dolphin.account.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {


	@RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	AuthUser createUser(AuthUser authUser);

	@RequestMapping(method = RequestMethod.POST, value = "/uaa/users/changepassword", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Boolean changePassword(@RequestBody ChangePasswordEntity entity);

	@RequestMapping(method = RequestMethod.DELETE, value = "/uaa/users/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	AuthUser deleteUser(@PathVariable("id") Integer id);
/*

	@Autowired
	private OAuth2RestOperations restTemplate;


	public void saveUser(AuthUser authUser){
		restTemplate.postForEntity("http://auth-service/uaa/users", authUser,AuthUser.class);
	}
*/
}
