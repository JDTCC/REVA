package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.Serializable;

/**
 * De <tt>TrieLeaf</tt> is een uiteinde van een <tt>TrieMap</tt> en bevat de
 * <tt>value</tt> die de <tt>TrieMap</tt> bijhoudt.
 * 
 * @param <V>
 *          Het type value dat deze leaf bevat.
 * 
 */
public class TrieLeaf<V extends Serializable> implements Serializable {

  private static final long serialVersionUID = 1L;
  private String key;
  private V value;

  /**
   * Maakt een <tt>TrieLeaf</tt> met <tt>key</tt> als sleutel en <tt>value</tt>
   * als waarde.
   * 
   * @param key
   *          De sleutel.
   * @param value
   *          De waarde.
   */
  public TrieLeaf( String key, V value ) {
    this.key = key;
    this.value = value;
  }

  /**
   * Geeft de sleutel van de <tt>TrieLeaf</tt>
   * 
   * @return De sleutel.
   */
  public String getKey() {
    return key;
  }

  /**
   * Geeft de waarde van de <tt>TrieLead</tt>
   * 
   * @return De waarde.
   */
  public V getValue() {
    return value;
  }
}
