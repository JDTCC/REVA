package nl.overheid.stelsel.digimelding.astore.notification.twitter;

import twitter4j.Twitter;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFactory {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String consumerKey;
  private String consumerSecret;
  private String accessToken;
  private String accessTokenSecret;

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public Twitter getInstance() {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
    return new twitter4j.TwitterFactory(cb.build()).getInstance();
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getConsumerKey() {
    return consumerKey;
  }

  public void setConsumerKey(String consumerKey) {
    this.consumerKey = consumerKey;
  }

  public String getConsumerSecret() {
    return consumerSecret;
  }

  public void setConsumerSecret(String consumerSecret) {
    this.consumerSecret = consumerSecret;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessTokenSecret() {
    return accessTokenSecret;
  }

  public void setAccessTokenSecret(String accessTokenSecret) {
    this.accessTokenSecret = accessTokenSecret;
  }
}
