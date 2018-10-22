package dolphin.account.controller;


import dolphin.account.domain.Account;
import dolphin.account.domain.permission.Permission;
import dolphin.account.domain.permission.PermissionRepository;
import dolphin.account.domain.role.Role;
import dolphin.account.domain.role.RoleRepository;
import dolphin.account.domain.user.User;
import dolphin.account.domain.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@Api("账号服务")
public class AccountController {

	private static Logger logger= LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;


	/*
	@PreAuthorize("#oauth2.hasScope('server') or #name.equals('demo')")
	@RequestMapping(path = "/{name}", method = RequestMethod.GET)
	public Account getAccountByName(@PathVariable String name)
	{
		User user= userRepository.findUserByName(name);
        Account account=new Account();
		account.setName(user.getName());
		//TODO

		account.setAuthorities(getUserAuth(user));
		return account;
	}
	*/
	@ApiOperation("获取当前登录用户信息")
	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Account getCurrentAccount(Principal principal)
	{
		User user= userRepository.findUserByName(principal.getName());
		if(user == null){
			logger.error("can't find user by name {}", principal.getName());
			Account account=new Account();
			account.setName(principal.getName());
			return account;
		}
		Account account=new Account();
		account.setName(user.getName());
		//TODO

		account.setAuthorities(getUserAuth(user));
		return account;
	}


	private Set<String> getUserAuth(User user) {
		Set<Integer> roleIds = user.getRoleIds();
		Set<String> uniqueAuth = new HashSet();
		for (int id : roleIds) {
			Role role = roleRepository.getOne(id);
			if (role != null) {
				Set<Integer> perIds = role.getPermissionIds();
				for (int pid : perIds) {
					Permission p = this.permissionRepository.findOne(pid);
					if (p != null) {
						uniqueAuth.add(p.getUniqueId());
					}
				}
			}

		}

		return uniqueAuth;
		//userClient.saveUserAuthority(JSON.toJSONString(map));
	}

}
