package ph.parcs.rmhometiles.entity.user;

import lombok.Builder;
import lombok.Data;
import ph.parcs.rmhometiles.util.AppConstant;


@Data
@Builder
public class UserData {

    private String username;
    private String fullName;
    private String password;
    private AppConstant.Role role;
}
