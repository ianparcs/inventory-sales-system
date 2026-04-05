package ph.parcs.rmhometiles.entity.user;

import javafx.beans.property.ObjectProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.util.AppConstant;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findUserByRole(AppConstant.Role role);
}
