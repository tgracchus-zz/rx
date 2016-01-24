import com.backend.app.dto.AuthorizationContext;
import com.backend.app.dto.LoginContext;
import com.backend.app.model.Credentials;
import com.backend.app.model.Principal;
import com.backend.app.model.Roles;
import com.backend.app.model.Session;
import com.backend.app.services.DefaultSessionService;
import com.backend.app.services.SessionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ulises.olivenza on 23/10/15.
 */
public class SessionServiceTest {


    private SessionService sessionService;
    private LoginContext loginContext;

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

        sessionService = new DefaultSessionService(10, credentialsByUser);

    }

    @Test
    public void testLogin() throws Exception {
        Observable<Optional<Session>> loginSession = sessionService.login(loginContext);

        loginSession.map(session -> {
            Assert.assertTrue(session.isPresent());
            Assert.assertEquals(loginContext.getUserId(), session.get().getUserId());
            return session;

        }).flatMap(session1 -> {
            AuthorizationContext authorizationContext = new AuthorizationContext(session1.get().getSessionId(), Roles.PAGE_1.getRole());
            return sessionService.authorize(authorizationContext);

        }).subscribe(bool -> Assert.assertTrue(bool));


    }

    @Test
    public void testLoginInvalidUser() throws Exception {
        sessionService.login(new LoginContext("invalidUser", loginContext.getPassword()))
                .subscribe(session -> Assert.assertFalse(session.isPresent()));


    }

    @Test
    public void testLoginInvalidPassword() throws Exception {
        sessionService.login(new LoginContext(loginContext.getUserId(), "invalidPassword"))
                .subscribe(session -> Assert.assertFalse(session.isPresent()));

    }

    @Test
    public void testInvalidUserPassword() throws Exception {
        sessionService.login(new LoginContext("invalidUser", "invalidPassword"))
                .subscribe(session -> Assert.assertFalse(session.isPresent()));

    }
/*
    @Test
    public void testLoginLogout() throws Exception {
        sessionService.login(loginContext)
                .map(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    return loginSession;
                }).map(session -> session);


        Optional<Session> logOutSession =
                Assert.assertTrue(logOutSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());

        Assert.assertEquals(loginSession.get(), logOutSession.get());

        AuthorizationContext authorizationContext = new AuthorizationContext(loginSession.get().getSessionId(), Roles.PAGE_1.getRole());
        Assert.assertFalse(sessionService.authorize(authorizationContext));

    }

    @Test
    public void testLoginTwice() throws Exception {
        Optional<Session> loginSession = sessionService.login(loginContext);

        Assert.assertTrue(loginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

        AuthorizationContext authorizationContext = new AuthorizationContext(loginSession.get().getSessionId(), Roles.PAGE_1.getRole());
        Assert.assertTrue(sessionService.authorize(authorizationContext));


        Optional<Session> secondLoginSession = sessionService.login(loginContext);

        Assert.assertTrue(secondLoginSession.isPresent());
        Assert.assertEquals(loginContext.getUserId(), secondLoginSession.get().getUserId());

        AuthorizationContext secondAuthorizationContext = new AuthorizationContext(loginSession.get().getSessionId(), Roles.PAGE_1.getRole());
        Assert.assertTrue(sessionService.authorize(secondAuthorizationContext));

        Assert.assertEquals(loginSession.get(), secondLoginSession.get());

    }

    @Test
    public void testLoginLogoutLogout() throws Exception {
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

    @Test
    public void testLoginLogoutLogin() throws Exception {
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

    @Test
    public void testLoginLoginLogout() throws Exception {
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

    }*/

}
