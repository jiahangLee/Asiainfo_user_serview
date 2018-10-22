package dolphin.account.controller;


import dolphin.account.client.AuthServiceClient;
import dolphin.account.domain.AuthUser;
import dolphin.account.domain.ChangePasswordEntity;
import dolphin.account.domain.permission.Permission;
import dolphin.account.domain.permission.PermissionRepository;
import dolphin.account.domain.role.Role;
import dolphin.account.domain.role.RoleRepository;
import dolphin.account.domain.user.User;
import dolphin.account.domain.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.comparator.BooleanComparator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Api("用户服务")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private AuthServiceClient authClient;

	@Value("${u4a.user.initialAdmin.username}")
	private String initialAdminUsername;

	@Value("${u4a.user.initialAdmin.password}")
	private String initialAdminPassword;

	private String initialAdminRoleName="ADMIN";


	@PostConstruct
	private void initialAdminUser(){
		/*AuthUser authUser = authUserRepository.findUserByUsername(initialAdminUsername);
		if(authUser ==null){
			authUser =new AuthUser(initialAdminUsername, initialAdminPassword,"");
			AuthRole authRole =new AuthRole(initialAdminRoleName);
			authRole = authRoleRepository.save(authRole);
			authUser.setAuthRoles(Sets.newHashSet(authRole));
			authUserRepository.save(authUser);
		}*/
		User user=userRepository.findUserByName(initialAdminUsername);
		Role role=roleRepository.findRoleByItemName(initialAdminRoleName);
		Permission permission=permissionRepository.findByUniqueId("ADMIN");
		if(permission==null){
			permission=new Permission();
			permission.setName("ADMIN");
			permission.setLabel("ADMIN");
			permission.setUniqueId("ADMIN");
			permission=permissionRepository.save(permission);
		}
		if(role == null){
			role=new Role();
			role.setGroupLabel(initialAdminRoleName);
			role.setGroupName(initialAdminRoleName);
			role.setItemLabel(initialAdminRoleName);
			role.setItemName(initialAdminRoleName);
			role.setEnable(true);
			role.setRemark("system internal, don't remove.");
			Set<Integer> set=new HashSet();
			set.add(permission.getId());
			role.setPermissionIds(set);
			role = roleRepository.save(role);
		}
		if(user==null){
			user=new User();
            user.setId(1);
			user.setAdmin(true);
			user.setName(initialAdminUsername);
			user.setEnable(true);
			user.setAdmin(true);

			user.setLabel(initialAdminUsername);
			user.setPhone("");
			Set<Integer> roleIds=new HashSet();
			roleIds.add(role.getId());
			user.setRoleIds(roleIds);
			user = this.userRepository.save(user);

			//authClient.saveUser(toAuthUser(user));
		}
	}
	/*
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}
**/

	@GetMapping
	public ResponseEntity<List<User>> list() {
		//ResponseEntity res=oAuth2RestTemplate.getForEntity("http://dolphin-auth-service/internal/user", ResponseEntity.class);
		// System.out.println(res.getBody());
		return new ResponseEntity<List<User>>(this.userRepository.findAll(), HttpStatus.OK);
	}

	@ApiOperation("创建用户")
	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		AuthUser authUser=authClient.createUser(toAuthUser(user));
		user.setId(authUser.getId());
		User result = this.userRepository.save(user);
		//processUserAuth(result);
		return new ResponseEntity<User>(result, HttpStatus.OK);
	}

	@ApiOperation("改变密码")
	@PostMapping("/changepassword")
	public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordEntity changePasswordEntity) {
		Boolean result=authClient.changePassword(changePasswordEntity);

		//processUserAuth(result);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@ApiOperation("更新用户信息，不包括密码")
	@PutMapping(path = "/{id}")
	public ResponseEntity<User> update(@RequestBody User user, @PathVariable("id") Integer id) {
		User result = this.userRepository.save(user);
		//processUserAuth(result);
		//authClient.saveUser(toAuthUser(result));
		return new ResponseEntity<User>(result, HttpStatus.OK);
	}


	private String processUserAuth(User user) {
		Set<Integer> roleIds = user.getRoleIds();
		StringBuffer uniqueAuth = new StringBuffer();
		for (int id : roleIds) {
			Role role = roleRepository.getOne(id);
			if (role != null) {
				Set<Integer> perIds = role.getPermissionIds();
				for (int pid : perIds) {
					Permission p = this.permissionRepository.findOne(pid);
					if (p != null) {
						uniqueAuth.append(p.getUniqueId()).append(",");
					}
				}
			}

		}
		if(uniqueAuth.length()>0){
			uniqueAuth.deleteCharAt(uniqueAuth.length()-1);
		}
		return uniqueAuth.toString();

		//userClient.saveUserAuthority(JSON.toJSONString(map));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<User> delete(@PathVariable("id") Integer id) {
		AuthUser authUser=authClient.deleteUser(id);

		User u = this.userRepository.findOne(id);
		if(u!=null) {
			this.userRepository.delete(id);
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}else{
			return new ResponseEntity<User>((User)null, HttpStatus.NOT_FOUND);
		}

		//userClient.deleteUser(u.getName());

	}
	@GetMapping(path = "/{name}")
	public ResponseEntity<User> get(@PathVariable("name") String name) {
		User u = this.userRepository.findUserByName(name);

		return new ResponseEntity<User>(u, HttpStatus.OK);
	}

	private AuthUser toAuthUser(User user){
		AuthUser authUser=new AuthUser();
		authUser.setId(user.getId());
		authUser.setUsername(user.getName());
		authUser.setPassword(user.getPassword());
		authUser.setAuthoritiesStr(processUserAuth(user));
		return authUser;
	}
}
