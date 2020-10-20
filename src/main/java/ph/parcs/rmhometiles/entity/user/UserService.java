package ph.parcs.rmhometiles.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private Authentication authenticationToken;
    private UserRepository userRepository;

    private User currentUser;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        currentUser = userRepository.findByUsername(username);

        if (currentUser == null || username.isEmpty()) {
            return null;
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(currentUser.getRole()));

        return new org.springframework.security.core.userdetails.User(currentUser.getUsername(), currentUser.getPassword(), grantedAuthorities);
    }

    public void authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails != null) {
            if (isPasswordMatch(password, userDetails.getPassword())) {
                authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
                authenticationManager.authenticate(authenticationToken);
                if (authenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private boolean isPasswordMatch(String plainPass, String encodedPass) {
        return bCryptPasswordEncoder.matches(plainPass, encodedPass);
    }

    public boolean isAuthenticated() {
        return authenticationToken != null && authenticationToken.isAuthenticated();
    }

    //TODO
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
