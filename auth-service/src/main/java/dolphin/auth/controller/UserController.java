package dolphin.auth.controller;


import dolphin.auth.domain.user.AuthUser;
import dolphin.auth.domain.user.AuthUserRepository;
import dolphin.auth.domain.user.ChangePasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private AuthUserRepository userRepository;
	@Value("${u4a.user.initialAdmin.username}")
	private String initialAdminUsername;

	@Value("${u4a.user.initialAdmin.password}")
	private String initialAdminPassword;
	private String adminRole="ADMIN";

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


	@PostConstruct
	private void initAdmin() throws Exception {
		AuthUser authUser=userRepository.findByUsername(initialAdminUsername);
		if(authUser==null){
			authUser=new AuthUser();
			authUser.setId(1);
			authUser.setUsername(initialAdminUsername);
			authUser.setPassword(encoder.encode(encodeSHA(initialAdminPassword.getBytes())));
			authUser.setAuthoritiesStr(adminRole);
			userRepository.save(authUser);
		}
	}


	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}




	@PreAuthorize("#oauth2.hasScope('server')")
	@PostMapping
	public ResponseEntity<AuthUser> create(@RequestBody AuthUser user) {
		user.setPassword(encoder.encode(user.getPassword()));
		AuthUser result = this.userRepository.save(user);
		return new ResponseEntity<AuthUser>(result, HttpStatus.OK);
	}
	@PreAuthorize("#oauth2.hasScope('server')")
	@PostMapping("/changepassword")
	public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordEntity entity){
		AuthUser result = this.userRepository.findByUsername(entity.getUsername());
		if(result!=null){
			if(encoder.matches(entity.getOldPassword(), result.getPassword())){
				result.setPassword(encoder.encode(entity.getNewPassword()));
				this.userRepository.save(result);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}else{
				return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
			}

		}else{
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}

	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<AuthUser> delete(@PathVariable("id") Integer id) {
		AuthUser u = this.userRepository.findOne(id);
		if(u!=null) {
			this.userRepository.delete(id);
			return new ResponseEntity<AuthUser>(u, HttpStatus.OK);
		}else{
			return  new ResponseEntity<AuthUser>((AuthUser)null, HttpStatus.NOT_FOUND);
		}
		//userClient.deleteUser(u.getName());

	}

	private static String encodeSHA(byte[] data) throws Exception {
		// 初始化MessageDigest,SHA即SHA-1的简称
		MessageDigest md = MessageDigest.getInstance("SHA");
		// 执行摘要方法
		byte[] digest = md.digest(data);
		return new HexBinaryAdapter().marshal(digest).toLowerCase();
	}
}
