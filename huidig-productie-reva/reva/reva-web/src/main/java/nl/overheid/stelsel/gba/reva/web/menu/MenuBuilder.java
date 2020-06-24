package nl.overheid.stelsel.gba.reva.web.menu;

import java.util.ArrayList;
import java.util.List;

import nl.overheid.stelsel.gba.reva.web.pages.HomePage;
import nl.overheid.stelsel.gba.reva.web.pages.beheer.BAGBeheerPage;
import nl.overheid.stelsel.gba.reva.web.pages.beheer.BeheerPage;
import nl.overheid.stelsel.gba.reva.web.pages.beheer.GebruikersBeheerPage;
import nl.overheid.stelsel.gba.reva.web.pages.beheer.GemeenteWoonplaatsBeheerPage;
import nl.overheid.stelsel.gba.reva.web.pages.rapportage.DoorGemeenteRapportagePage;
import nl.overheid.stelsel.gba.reva.web.pages.rapportage.RapportagePage;
import nl.overheid.stelsel.gba.reva.web.pages.rapportage.VoorGemeenteRapportagePage;
import nl.overheid.stelsel.gba.reva.web.pages.toevoegen.InvoerenDossierNummerPage;
import nl.overheid.stelsel.gba.reva.web.pages.zoeken.ZoekenPage;
import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.security.RevaRoles;

public final class MenuBuilder {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static List<MenuItem> menuItems = null;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private MenuBuilder() {
    // Hide to prevent instantiation.
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public static synchronized List<MenuItem> build() {
    if( menuItems == null ) {
      menuItems = new ArrayList<>();

      // Home menu items
      MenuItem parent = null;
      menuItems.add( new SimpleMenuItem( "home", HomePage.class ) );

      // Opvoeren menu items
      parent = null;
      menuItems.add( new SecuredMenuItem( "invoeren", InvoerenDossierNummerPage.class, new RevaRoles[] {},
              new RevaPermissions[] { RevaPermissions.OPVOEREN_EVA } ) );

      // Zoeken menu items
      parent = null;
      menuItems.add( new SecuredMenuItem( "zoeken", ZoekenPage.class, new RevaRoles[] {},
              new RevaPermissions[] { RevaPermissions.ZOEKEN_BASIS } ) );

      // Rapportage menu items
      parent = new SecuredMenuItem( "rapportage", RapportagePage.class, new RevaRoles[] { RevaRoles.COORDINATOR,
          RevaRoles.BEHEERDER }, new RevaPermissions[] {} );
      new SecuredMenuItem( "doorLoketGemeente", DoorGemeenteRapportagePage.class, parent, new RevaRoles[] {},
              new RevaPermissions[] { RevaPermissions.RAPPORTAGE_DOOR_LOKETGEMEENTE } );
      new SecuredMenuItem( "voorDoelGemeente", VoorGemeenteRapportagePage.class, parent, new RevaRoles[] {},
              new RevaPermissions[] { RevaPermissions.RAPPORTAGE_VOOR_DOELGEMEENTE } );
      menuItems.add( parent );

      // Beheer menu items
      parent = new SecuredMenuItem( "beheer", BeheerPage.class, new RevaRoles[] { RevaRoles.BEHEERDER }, new RevaPermissions[] {} );
      menuItems.add( parent );
      new SecuredMenuItem( "gebruikersBeheer", GebruikersBeheerPage.class, parent, new RevaRoles[] { RevaRoles.BEHEERDER },
              new RevaPermissions[] {} );
      new SecuredMenuItem( "bagBeheer", BAGBeheerPage.class, parent, new RevaRoles[] { RevaRoles.BEHEERDER },
              new RevaPermissions[] {} );
      new SecuredMenuItem( "gemeenteWoonplaatBeheer", GemeenteWoonplaatsBeheerPage.class, parent,
              new RevaRoles[] { RevaRoles.BEHEERDER }, new RevaPermissions[] {} );
    }

    return menuItems;
  }
}
