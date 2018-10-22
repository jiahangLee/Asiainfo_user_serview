/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dolphin.auth.service;


import dolphin.auth.domain.user.AuthUser;
import dolphin.auth.domain.user.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
	private AuthUserRepository authUserRepository;



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	/*
		AuthUser authUser = authUserRepository.findUserByUsername(username);
		if (authUser == null) {
			throw new UsernameNotFoundException(String.format("AuthUser %s does not exist!", username));
		}
		return new UserRepositoryAuthUserDetails(authUser);
		*/
		AuthUser user = authUserRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("AuthUser %s does not exist!", username));
		}
		return user;
	}


}
