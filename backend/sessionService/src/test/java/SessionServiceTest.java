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

    @Test
    public void testLoginLogout() throws Exception {
        sessionService.login(loginContext)
                .map(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    return loginSession;
                }).subscribe(loginSession -> {
                    sessionService.logout(loginSession.get().getSessionId())
                            .subscribe(logOutSession -> {
                                Assert.assertEquals(loginSession.get(), logOutSession.get());
                                Assert.assertTrue(logOutSession.isPresent());
                                Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());
                            });
                }
        );

    }


    @Test
    public void testLoginTwice() throws Exception {
        sessionService.login(loginContext)
                .subscribe(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    AuthorizationContext authorizationContext = new AuthorizationContext(loginSession.get().getSessionId(), Roles.PAGE_1.getRole());
                    sessionService.authorize(authorizationContext);

                    sessionService.login(loginContext)
                            .subscribe(secondLoginSession -> {
                                Assert.assertTrue(secondLoginSession.isPresent());
                                Assert.assertEquals(loginContext.getUserId(), secondLoginSession.get().getUserId());

                                AuthorizationContext secondAuthorizationContext = new AuthorizationContext(loginSession.get().getSessionId(), Roles.PAGE_1.getRole());
                                sessionService.authorize(secondAuthorizationContext).subscribe(aBoolean ->
                                        Assert.assertTrue(aBoolean)
                                );
                                Assert.assertEquals(loginSession.get(), secondLoginSession.get());
                            });

                });


    }

    @Test
    public void testLoginLogoutLogout() throws Exception {
        sessionService.login(loginContext)
                .subscribe(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    sessionService.logout(loginSession.get().getSessionId())
                            .subscribe(logOutSession -> {
                                Assert.assertTrue(logOutSession.isPresent());
                                Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());
                                Assert.assertEquals(loginSession.get(), logOutSession.get());

                                sessionService.logout(loginSession.get().getSessionId()).subscribe(retryLogout ->
                                        Assert.assertFalse(retryLogout.isPresent())
                                );
                            });
                });
    }

    @Test
    public void testLoginLogoutLogin() throws Exception {
        sessionService.login(loginContext)
                .subscribe(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    sessionService.logout(loginSession.get().getSessionId())
                            .subscribe(logOutSession -> {
                                Assert.assertTrue(logOutSession.isPresent());
                                Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());
                                Assert.assertEquals(loginSession.get(), logOutSession.get());
                                sessionService.login(loginContext).subscribe(secondLoginSession -> {
                                    Assert.assertTrue(secondLoginSession.isPresent());
                                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());

                                    Assert.assertNotEquals(logOutSession, secondLoginSession);
                                    Assert.assertNotEquals(loginSession, secondLoginSession);
                                });

                            });
                });
    }

    @Test
    public void testLoginLoginLogout() throws Exception {
        sessionService.login(loginContext)
                .subscribe(loginSession -> {
                    Assert.assertTrue(loginSession.isPresent());
                    Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                    sessionService.login(loginContext).subscribe(secondLoginSession -> {
                        Assert.assertTrue(secondLoginSession.isPresent());
                        Assert.assertEquals(loginContext.getUserId(), loginSession.get().getUserId());
                        sessionService.logout(loginSession.get().getSessionId())
                                .subscribe(logOutSession -> {
                                    Assert.assertTrue(logOutSession.isPresent());
                                    Assert.assertEquals(loginContext.getUserId(), logOutSession.get().getUserId());
                                });
                        sessionService.logout(secondLoginSession.get().getSessionId())
                                .subscribe(secondLogOutSession -> Assert.assertFalse(secondLogOutSession.isPresent()));

                    });
                });
    }

}
