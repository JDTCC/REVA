package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * De <tt>TrieMap</tt>, ook wel bekend als PrefixMap, is een datastructuur die
 * het mogelijk maakt om op gedeeltelijke sleutel te zoeken.
 * 
 * @param <E>
 *          Het type element waaruit de <tt>TrieMap</tt> bestaat.
 * 
 */
public class TrieMap<E extends Serializable> implements Serializable {

  private static final long serialVersionUID = 1L;
  private TrieLeaf<E> value;
  private Map<Character, TrieMap<E>> children = new HashMap<>();

  public TrieMap() {
    // Default constructor.
  }

  /**
   * Voegt de <tt>value</tt> toe onder sleutel <tt>key</tt> aan deze TrieMap.
   * Als de <tt>key</tt> al bestaat, dan wordt de <tt>value</tt> met de nieuwe
   * waarde overschreven.
   * 
   * @param key
   *          De sleutel.
   * @param value
   *          De waarde.
   */
  public void put( String key, E value ) {
    assert key != null;
    put( key.toUpperCase(), new TrieLeaf<>( key, value ) );
  }

  private void put( String key, TrieLeaf<E> value ) {
    if( key.length() == 0 ) {
      this.value = value;
    } else {
      TrieMap<E> child = children.get( key.charAt( 0 ) );
      if( child == null ) {
        child = new TrieMap<>();
        children.put( key.charAt( 0 ), child );
      }
      child.put( key.substring( 1 ), value );
    }
  }

  /**
   * Geeft de bij de sleutel horende waarde terug of <tt>null</tt> als de
   * sleutel niet bekend is.
   * 
   * @param key
   *          De sleutel.
   * @return De waarde behorende bij de sleutel.
   */
  public E get( String key ) {
    assert key != null;

    String keyUpper = key.toUpperCase();

    if( keyUpper.length() == 0 ) {
      return value == null ? null : value.getValue();
    }

    TrieMap<E> child = children.get( keyUpper.charAt( 0 ) );
    if( child != null ) {
      return child.get( keyUpper.substring( 1 ) );
    }

    return null;
  }

  /**
   * Geeft een lijst van sleutels die volgen op de opgegeven (gedeeltelijke)
   * sleutel.
   * 
   * @param key
   *          De (gedeeltelijke) sleutel.
   * @return De lijst van volg sleutels.
   */
  public List<String> tail( String key ) {
    List<String> tail = new ArrayList<>();
    assert key != null;

    String keyUpper = key.toUpperCase();

    if( keyUpper.length() == 0 ) {
      for( TrieMap<E> child : children.values() ) {
        tail.addAll( child.tail( keyUpper ) );
      }

      if( value != null ) {
        tail.add( value.getKey() );
      }
    } else {
      TrieMap<E> child = children.get( keyUpper.charAt( 0 ) );
      if( child != null ) {
        tail.addAll( child.tail( keyUpper.substring( 1 ) ) );
      }
    }

    return tail;
  }

  public Collection<TrieLeaf<E>> leafs() {
    Collection<TrieLeaf<E>> leafs = new ArrayList<>();

    if( value != null ) {
      leafs.add( value );
    }

    for( TrieMap<E> triemap : children.values() ) {
      leafs.addAll( triemap.leafs() );
    }

    return leafs;
  }
}
