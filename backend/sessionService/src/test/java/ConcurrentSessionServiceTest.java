import com.schibsted.backend.app.dto.LoginContext;
import com.schibsted.backend.app.model.*;
import com.schibsted.backend.app.services.DefaultSessionService;
import com.schibsted.backend.app.model.SessionHealthCheck;
import com.schibsted.backend.app.services.SessionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class ConcurrentSessionServiceTest {

    private SessionService sessionService;
    private LoginContext loginContext;

    private List<ImpatientUser> impatientUsers;

    @Before
    public void setUp() throws Exception {
        loginContext = new LoginContext("user1", "password1");

        Credentials user1Credentials = new Credentials.CredentialsBuilder()
                .principal(new Principal(loginContext.getUserId(), loginContext.getPassword()))
                .role(Roles.PAGE_1)
                .role(Roles.PAGE_2)
                .build();

        ConcurrentMap<String, Credentials> credentialsByUser = new ConcurrentHashMap<>();
        credentialsByUser.put(user1Credentials.getUserId(), user1Credentials);

        sessionService = new DefaultSessionService(1, credentialsByUser);

        impatientUsers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            impatientUsers.add(new ImpatientUser(sessionService, loginContext));
        }

    }


    private static class ImpatientUser implements Runnable {

        private final SessionService sessionService;
        private final LoginContext loginContext;

        public ImpatientUser(SessionService sessionService, LoginContext loginContext) {
            this.sessionService = sessionService;
            this.loginContext = loginContext;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                Optional<Session> loginSession = sessionService.login(loginContext);
                Assert.assertTrue(loginSession.isPresent());
                Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

                sessionService.logout(loginSession.get().getSessionId());

            }

        }
    }

    @Test
    public void testLoginLogout() throws Exception {

        for (int i = 0; i < 100; i++) {
            Optional<Session> loginSession = sessionService.login(loginContext);

            AssertConcurrent.assertConcurrent("Finish Impatient User", impatientUsers, 120);

            Optional<Session> secondLoginSession = sessionService.login(loginContext);
            Optional<Session> logOutSession = sessionService.logout(secondLoginSession.get().getSessionId());

            SessionHealthCheck sessionHealthCheck = sessionService.healtcheck();
            Assert.assertEquals(0, sessionHealthCheck.getNumberOfUsers());
            Assert.assertEquals(0, sessionHealthCheck.getNumberOfSessions());

            Assert.assertNotEquals(loginSession.get(), secondLoginSession.get());

        }
    }


}
