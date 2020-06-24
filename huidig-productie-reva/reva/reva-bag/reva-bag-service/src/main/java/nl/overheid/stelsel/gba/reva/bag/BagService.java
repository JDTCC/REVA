package nl.overheid.stelsel.gba.reva.bag;

import java.util.Collection;
import java.util.Properties;

import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCOpenbareRuimte;
import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;

/**
 * De service om BAG gegevens te benaderen.
 * 
 */
public interface BagService {

  /**
   * Controleert of de opgegeven woonplaats in de bag voorkomt.
   * 
   * @param woonplaatsnaam
   *          de naam van de woonplaats.
   * @return True indien de naam voorkomt als woonplaats in de bag, false
   *         otherwise.
   * 
   * @throws BagException
   *           indien er fouten optreden.
   */
  boolean isWoonplaatsnaam( String woonplaatsnaam );

  /**
   * Geeft de woonplaats terug indien de opgegeven woonplaats in de bag voorkomt.
   * 
   * @param woonplaatsnaam
   *          de naam van de woonplaats.
   * @return BCWoonplaats indien deze in de BAG voorkomt.
   * 
   * @throws BagException
   *           indien er fouten optreden.
   */
  BCWoonplaats getWoonplaats( String woonplaatsnaam );

  /**
   * Geeft een lijst met woonplaatsen terug die overeenkomen met de opgegeven
   * gedeeltelijke woonplaats naam
   * 
   * @param partialWoonplaatsnaam
   *          deel van de woonplaats naam
   * @return Een lijst met woonplaatsen die overeenkomen met het opgegeven deel.
   *         Deze lijst kan leeg zijn indien er geen plaatsnamen zijn die
   *         overeen komen.
   */
  Collection<BCWoonplaats> getWoonplaatsen( String partialWoonplaatsnaam );

  /**
   * Controleert of de opgegeven openbare ruimte in de opgegeven woonplaats in
   * de bag voorkomt.
   * 
   * @param woonplaatsnaam
   *          de naam van de woonplaats.
   * @param openbareruimtenaam
   *          de naam van de openbare ruimte.
   * @return True indien de naam voorkomt als openbare ruimte in de woonplaats
   *         in de bag, false otherwise.
   * 
   * @throws BagException
   *           indien er fouten optreden.
   */
  boolean isOpenbareruimtenaam( String woonplaatsnaam, String openbareruimtenaam );

  /**
   * Geeft een lijst met woonplaatsen terug die overeenkomen met de opgegeven
   * gedeeltelijke woonplaats naam
   * 
   * @param woonplaats
   *          de woonplaats waarbinnen openbare ruimtes moeten liggen.
   * @param partialOpenbareruimtenaam
   *          deel van de openbare ruimte naam
   * @return Een lijst met openbare ruimtes die overeenkomen met het opgegeven
   *         deel. Deze lijst kan leeg zijn indien er geen openbare ruimtes zijn
   *         die overeen komen.
   */
  Collection<BCOpenbareRuimte> getOpenbareruimtes( String woonplaats, String partialOpenbareruimtenaam );

  /**
   * Geeft alle BAG adressen die aan de opgegeven criteria voldoen.
   * 
   * @param woonplaatsnaam
   *          de naam van de woonplaats van het adres.
   * @param openbareruimtenaam
   *          de naam van de openbare ruimte van het adres.
   * @param postcode
   *          de postcode van het adres
   * @param huisnummer
   *          het huisnummer van het adres
   * @param huisletter
   *          de huisletter van het adres
   * @param huisnummerToevoeging
   *          de huisnummer toevoeging
   * @return Een lijst met adres object die voldoen aan de opgegeven criteria.
   *         Deze lijst kan leeg zijn indien er geen BAG adressen zijn die aan
   *         de opgegeven criteria voldoen.
   */
  Collection<BCAdres> getAdressen( String woonplaatsnaam, String openbareruimtenaam, String postcode, String huisnummer,
          String huisletter, String huisnummerToevoeging );

  /**
   * Maakt het mogelijk om de interne data van de bag service te resetten. De
   * exacte werking hangt af van de BAG service implementatie.
   */
  void reset();

  /**
   * Geeft de meta informatie behorende bij de BAG service.
   * 
   * @return Properties bestand met meta informatie.
   */
  Properties getMetaInfo();
}
