package io.github.fire.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class EurekaClientDetailsService implements UserDetailsService {
	
	
	private final  Map<String, Map<String, String>> clientsAccount = new HashMap<String, Map<String,String>>();
	
	public EurekaClientDetailsService() {
		Map<String, String> account = new HashMap<String, String>();
		account.put("username", "registration-center");
		account.put("password", "registration-center");
		account.put("role", "admin");
		clientsAccount.put(account.get("username"), account);
		
		account = new HashMap<String, String>();
		account.put("username", "serving");
		account.put("password", "serving");
		account.put("role", "admin");
		clientsAccount.put(account.get("username"), account);
		
		account = new HashMap<String, String>();
		account.put("username", "agent");
		account.put("password", "agent");
		account.put("role", "admin");
		clientsAccount.put(account.get("username"), account);
		
		account = new HashMap<String, String>();
		account.put("username", "web");
		account.put("password", "web");
		account.put("role", "admin");
		
		clientsAccount.put(account.get("username"), account);
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, String> account = clientsAccount.get(username);
        String password = passwordEncoder().encode(account.get("password"));
        // 参数分别是：用户名，密码，用户权限
        User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(account.get("role")));
        return user;

	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	     return new BCryptPasswordEncoder();
	 }

}
