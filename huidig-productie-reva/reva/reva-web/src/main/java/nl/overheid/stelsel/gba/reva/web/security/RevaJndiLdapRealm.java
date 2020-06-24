package nl.overheid.stelsel.gba.reva.web.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shiro Realm voor LDAP met ondersteuning van de REVA rechtenstructuur.
 * 
 */
public class RevaJndiLdapRealm extends JndiLdapRealm {

  private static final Logger LOG = LoggerFactory.getLogger( RevaJndiLdapRealm.class );
  private static final String GEMEENTECODE_ATTRIBUUT = "o";

  private String roleSearchBase = "ou=Rollen";

  // --------------------------------------------------------------------------
  // Constructor
  // --------------------------------------------------------------------------

  public RevaJndiLdapRealm() {
    // Empty default constructor.
  }

  public void setRoleSearchBase( String roleSearchBase ) {
    this.roleSearchBase = roleSearchBase;

    if( LOG.isDebugEnabled() ) {
      LOG.debug( "roleSearchBase: {}", roleSearchBase );
    }
  }

  // --------------------------------------------------------------------------
  // Method overrides
  // --------------------------------------------------------------------------

  /**
   * {@inheritdoc}
   */
  @Override
  protected AuthenticationInfo createAuthenticationInfo( AuthenticationToken token, Object ldapPrincipal, Object ldapCredentials,
          LdapContext ldapContext ) throws NamingException {
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo( token.getPrincipal(), token.getCredentials(),
            getName() );

    String userDN = getUserDn( (String) token.getPrincipal() );
    String searchBase = userDN.substring( 0, userDN.indexOf( ",dc" ) );

    if( LOG.isDebugEnabled() ) {
      LOG.debug( "Searchbase voor gebruikersattributen: {}", searchBase );
    }

    Attributes attributen = ldapContext.getAttributes( searchBase );
    if( attributen != null ) {
      String gemeentecode = (String) attributen.get( GEMEENTECODE_ATTRIBUUT ).get( 0 );

      if( LOG.isDebugEnabled() ) {
        LOG.debug( "Gevonden gemeentecode: {}", gemeentecode );
      }

      SimplePrincipalCollection principalCollection = new SimplePrincipalCollection( authenticationInfo.getPrincipals() );
      principalCollection.add( "gc=" + gemeentecode, getName() );

      authenticationInfo.setPrincipals( principalCollection );
    }

    return authenticationInfo;
  }

  /**
   * {@inheritdoc}
   */
  @Override
  protected AuthorizationInfo queryForAuthorizationInfo( PrincipalCollection principals, LdapContextFactory ldapContextFactory )
          throws NamingException {
    Collection<String> rollen = new ArrayList<>();
    Collection<String> permissies = new ArrayList<>();

    String username = (String) getAvailablePrincipal( principals );

    if( LOG.isDebugEnabled() ) {
      LOG.debug( "Opzoek in LDAP naar rechten voor gebruiker: {}", username );
    }

    LdapContext ldapContext = ldapContextFactory.getSystemLdapContext();

    try {
      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE );

      String filterExpression = "(&(objectClass=*)(uniqueMember=" + getUserDnTemplate() + "))";
      Object[] filterArguments = new Object[] { username };

      NamingEnumeration<SearchResult> rollenEnumeration = ldapContext.search( roleSearchBase, filterExpression, filterArguments,
              searchControls );
      while( rollenEnumeration.hasMore() ) {
        SearchResult searchResult = rollenEnumeration.next();

        Attributes attributen = searchResult.getAttributes();
        if( attributen != null ) {
          String rol = (String) attributen.get( "cn" ).get( 0 );
          String description = (String) attributen.get( "description" ).get( 0 );

          if( LOG.isDebugEnabled() ) {
            LOG.debug( "Gebruiker heeft rol [{}] met bijbehorende permissies: {}", rol, description );
          }

          rollen.add( rol );
          for( String permissie : description.split( "," ) ) {
            permissies.add( permissie );
          }
        }
      }
    } finally {
      LdapUtils.closeContext( ldapContext );
    }

    SimpleAuthorizationInfo autorisatie = new SimpleAuthorizationInfo();
    autorisatie.addRoles( rollen );
    autorisatie.addStringPermissions( permissies );

    return autorisatie;
  }
}
