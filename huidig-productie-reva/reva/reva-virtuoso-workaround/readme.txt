Deze reva module is onderdeel van een work around voor de volgende Virtuoso meldingen:

https://github.com/openlink/virtuoso-opensource/issues/489
https://github.com/openlink/virtuoso-opensource/issues/338

De workaround betreft het toevoegen van een extra laatste adres marker. Hierdoor kunnen we de query op deze marker doen in plaats op de laatste timestamp.

Dit component voegt deze marker toe bij nieuwe adressen (tijdens preprocess) en verwijdert de marker bij oude adresssen (tijdens postprocess).

Gevolg is dat er een kleine timeframe is waar er meer dan 1 marker in de data zit. 
