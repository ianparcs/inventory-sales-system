package ph.parcs.rmhometiles.session;

import lombok.Getter;
import ph.parcs.rmhometiles.entity.user.User;

public class SessionService {

    @Getter
    private static final SessionService instance = new SessionService();

    private User loggedInUser;

    private SessionService() {
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
}