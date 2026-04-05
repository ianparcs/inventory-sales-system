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
import ph.parcs.rmhometiles.exception.AppException;
import ph.parcs.rmhometiles.exception.ExceptionType;
import ph.parcs.rmhometiles.util.AppConstant;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private Authentication authenticationToken;
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null || username.isEmpty()) {
            return null;
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(currentUser.getRole().name()));

        return new org.springframework.security.core.userdetails.User(currentUser.getUsername(), currentUser.getPassword(), grantedAuthorities);
    }

    public void authenticate(String username, String password) throws AppException {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) throw new AppException(ExceptionType.USER_NOT_EXIST);
        if (!bCryptPasswordEncoder.matches(password, userDetails.getPassword()))
            throw new AppException(ExceptionType.PASSWORD_INCORRECT);

        authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    @Transactional
    public void createUser(UserData userData) {
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setName(userData.getFullName());
        user.setRole(userData.getRole());

        userRepository.save(user);
    }

    @Transactional
    public void createAdminUser() {
        User adminUser = userRepository.findUserByRole(AppConstant.Role.ADMIN);
        if (adminUser == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("admin"));
            admin.setRole(AppConstant.Role.ADMIN);
            userRepository.save(admin);
        }
    }

    public boolean isAuthenticated() {
        return authenticationToken != null && authenticationToken.isAuthenticated();
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
