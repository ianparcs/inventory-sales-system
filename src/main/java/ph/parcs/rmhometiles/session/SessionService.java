package ph.parcs.rmhometiles.session;

import lombok.Data;
import lombok.Getter;
import ph.parcs.rmhometiles.entity.user.User;

@Data
public class SessionService {

    @Getter
    private static final SessionService instance = new SessionService();

    private User loggedInUser;

    private SessionService() {
    }

}