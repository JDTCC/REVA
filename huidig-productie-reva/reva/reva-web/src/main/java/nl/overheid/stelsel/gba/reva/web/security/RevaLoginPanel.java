package nl.overheid.stelsel.gba.reva.web.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.wicketstuff.shiro.component.LoginPanel;

/**
 * Eigen Login panel zodat we de markup kunnen overrulen.
 * 
 */
public class RevaLoginPanel extends LoginPanel {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 8156482402560929063L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RevaLoginPanel( final String id ) {
    this( id, true );
  }

  public RevaLoginPanel( final String id, final boolean includeRememberMe ) {
    super( id, includeRememberMe );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public boolean login( final String username, final String password, final boolean rememberMe ) {
    final Subject currentUser = SecurityUtils.getSubject();
    final UsernamePasswordToken token = new UsernamePasswordToken( username, password, rememberMe );
    try {
      currentUser.login( token );
      return true;

      // the following exceptions are just a few you can catch and handle
      // accordingly. See the
      // AuthenticationException JavaDoc and its subclasses for more.
    } catch( final IncorrectCredentialsException ice ) {
      error( getString( "login.incorrectCredentials" ) );
    } catch( final UnknownAccountException uae ) {
      error( getString( "login.unknownAccount" ) );
    } catch( final AuthenticationException ae ) {
      error( getString( "login.authentication" ) );
    } catch( final Exception ex ) {
      error( getString( "login.failed" ) );
    }
    return false;
  }

}
