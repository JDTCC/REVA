package nl.overheid.stelsel.gba.reva.web.security;

/**
 * Opsomming van alle gebruikte permissies binnen Reva.
 * 
 */
public enum RevaPermissions {
  RAADPLEGEN_DETAIL( "raadplegen:detail" ), RAADPLEGEN_BSN( "raadplegen:bsn" ), OPVOEREN_EVA( "opvoeren:eva" ), OPVOEREN_BSN(
          "opvoeren:bsn" ), OPVOEREN_NIEUW_ADRES( "opvoeren:nieuwAdres" ), ZOEKEN_BASIS( "zoeken:basis" ), ZOEKEN_BSN( "zoeken:bsn" ), 
          ZOEKEN_ALLES( "zoeken:alles" ), VERWIJDEREN_REGISTRATIE( "verwijderen:registratie" ), RAPPORTAGE_OPVRAGEN( "rapportage:opvragen" ), 
          RAPPORTAGE_GEMEENTECODE_VRIJ( "rapportage:gemeenteCodeVrij" ), RAPPORTAGE_DOOR_LOKETGEMEENTE( "query:rapportageDoorLoketGemeente" ), 
          RAPPORTAGE_VOOR_DOELGEMEENTE( "query:rapportageVoorDoelGemeente" );

  private String permission;

  private RevaPermissions( String permission ) {
    this.permission = permission;
  }

  public String getStringPermission() {
    return permission;
  }
}