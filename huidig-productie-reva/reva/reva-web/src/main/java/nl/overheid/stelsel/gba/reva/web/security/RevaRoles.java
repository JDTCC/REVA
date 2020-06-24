package nl.overheid.stelsel.gba.reva.web.security;

public enum RevaRoles {
  BALIE_MEDEWERKER( "balieMedwerker" ), GBA_MEDEWERKER( "gbaMedewerker" ), HANDHAVER( "handhaver" ), COORDINATOR( "coordinator" ), BEHEERDER(
          "beheerder" );

  private String role;

  private RevaRoles( String role ) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
