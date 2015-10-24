import com.schibsted.backend.app.dto.AuthorizationContext;
import com.schibsted.backend.app.dto.LoginContext;
import com.schibsted.backend.app.model.*;
import com.schibsted.backend.app.services.DefaultSessionService;
import com.schibsted.backend.app.services.SessionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class SessionServiceTest {


    private SessionService sessionService;
    private LoginContext loginContext;

    @Before public void setUp() throws Exception {

        loginContext = new LoginContext("user1", "password1");

        Credentials user1Credentials = new Credentials.CredentialsBuilder()
                .principal(new Principal(loginContext.getUserId(), loginContext.getPassword()))
                .role(Roles.PAGE_1)
                .role(Roles.PAGE_2)
                .build();

        ConcurrentMap<String, Credentials> credentialsByUser = new ConcurrentHashMap<>();
        credentialsByUser.put(user1Credentials.getUserId(), user1Credentials);

        sessionService = new DefaultSessionService(1, credentialsByUser);

        sessionService = new DefaultSessionService(10, credentialsByUser);

    }

    @Test public void testLogin() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);

        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());


        AuthorizationContext authorizationContext  = new AuthorizationContext(loginSession.get().getSessionId(),Roles.PAGE_1.getRole());
        Assert.assertTrue(sessionService.authorize(authorizationContext));
    }

    @Test public void testLoginInvalidUser() throws Exception {
        Optional<Session> loginSession = sessionService.login(new LoginContext("invalidUser", loginContext.getPassword()));

        Assert.assertFalse(loginSession.isPresent());

    }

    @Test public void testLoginInvalidPassword() throws Exception {
        Optional<Session> loginSession = sessionService.login(new LoginContext(loginContext.getUserId(), "invalidPassword"));
        Assert.assertFalse(loginSession.isPresent());

    }

    @Test public void testInvalidUserPassword() throws Exception {
        Optional<Session> loginSession = sessionService.login(new LoginContext("invalidUser", "invalidPassword"));
        Assert.assertFalse(loginSession.isPresent());
    }

    @Test public void testLoginLogout() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);
        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Optional<Session> logOutSession = sessionService.logout(loginSession.get().getSessionId());
        Assert.assertTrue(logOutSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());

        Assert.assertEquals(loginSession.get(), logOutSession.get());

        AuthorizationContext authorizationContext  = new AuthorizationContext(loginSession.get().getSessionId(),Roles.PAGE_1.getRole());
        Assert.assertFalse(sessionService.authorize(authorizationContext));

    }

    @Test public void testLoginTwice() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);

        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        AuthorizationContext authorizationContext  = new AuthorizationContext(loginSession.get().getSessionId(),Roles.PAGE_1.getRole());
        Assert.assertTrue(sessionService.authorize(authorizationContext));


        Optional<Session> secondLoginSession = sessionService.login(loginContext);

        Assert.assertTrue(secondLoginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), secondLoginSession.get().getUserId());

        AuthorizationContext secondAuthorizationContext  = new AuthorizationContext(loginSession.get().getSessionId(),Roles.PAGE_1.getRole());
        Assert.assertTrue(sessionService.authorize(secondAuthorizationContext));

        Assert.assertEquals(loginSession.get(), secondLoginSession.get());

    }

    @Test public void testLoginLogoutLogout() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);
        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Optional<Session> logOutSession = sessionService.logout(loginSession.get().getSessionId());
        Assert.assertTrue(logOutSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());

        Assert.assertEquals(loginSession.get(), logOutSession.get());

        Optional<Session> retryLogout = sessionService.logout(loginSession.get().getSessionId());
        Assert.assertFalse(retryLogout.isPresent());

    }

    @Test public void testLoginLogoutLogin() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);
        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Optional<Session> logOutSession = sessionService.logout(loginSession.get().getSessionId());
        Assert.assertTrue(logOutSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());

        Assert.assertEquals(loginSession.get(), logOutSession.get());

        Optional<Session> secondLoginSession = sessionService.login(loginContext);
        Assert.assertTrue(secondLoginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Assert.assertNotEquals(logOutSession, secondLoginSession);
        Assert.assertNotEquals(loginSession, secondLoginSession);

    }

    @Test public void testLoginLoginLogout() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);
        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Optional<Session> secondLoginSession = sessionService.login(loginContext);
        Assert.assertTrue(secondLoginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        Optional<Session> logOutSession = sessionService.logout(loginSession.get().getSessionId());
        Assert.assertTrue(logOutSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());

        Optional<Session> secondLogOutSession = sessionService.logout(secondLoginSession.get().getSessionId());
        Assert.assertFalse(secondLogOutSession.isPresent());

    }

}
