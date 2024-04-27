
/** Klasse Autokennzeichen (Informatik 5, S. 27/2) 
 **/
 
enum ZUSTAND { kfz, bezirk1, bezirk2, bezirk3, leer1, buchstabe1, buchstabe2, leer2, ziffer1, ziffer2, ziffer3, ziffer4, ungueltig };

public class Autokennzeichen
{
   private ZUSTAND aktuellerZustand;

   public Autokennzeichen()
   {
   }

    /**
     * der eingegebene Text wird darauf untersucht, ob er ein gültiges deutsches KFZ-Kennzeichen ist
     * es wird aber nicht überprüft, ob das Landkreiskennzeichen existiert (z.B. wird "ÄÄÄ B 14" erlaubt)
     * 
     * @param  kennzeichen  das zu überprüfende Kennzeichen
     * @return  true falls das Kennzeichen gültig ist, false sonst
     */
   public boolean istKennzeichenGueltig(String kennzeichen)
   {
      // aktuellen Zustand auf Startzustand setzen
      aktuellerZustand = ZUSTAND.kfz;

      // für alle Zeichen die Methode zustandWechseln aufrufen
      for(int i = 0; i < kennzeichen.length() && aktuellerZustand != ZUSTAND.ungueltig; i++) {
         String fehler = zustandWechseln(kennzeichen.charAt(i));
         if(fehler != null) {
            println("Nach \"" + kennzeichen.substring(0, i) + "\" wurde nicht ein " + fehler + " wie '" + kennzeichen.charAt(i) + "' erwartet.");
         }
         if(aktuellerZustand == ZUSTAND.ungueltig) return false;
      }
     
      // Rückgabe true, wenn ein Endzustand erreicht ist, sonst falsch
      switch(aktuellerZustand) {
         case ziffer1 : 
         case ziffer2 : 
         case ziffer3 : 
         case ziffer4 : 
            return true;
         default : 
            println("Das Kennzeichen ist zu kurz.");
            return false;
             
      }
      return false;
   }
    
    /**
     * je nach aktuellem Zustand und dem eingegebenen Zeichen wird der Zustand gewechselt
     * Implementierung des endlichen Automaten zur KFZ-Kennzeichen-Erkennung
     * 
     * @param  kennzeichen  das zu überprüfende Kennzeichen
     * @return  true falls das Kennzeichen gültig ist, sonst false 
     */
   private String zustandWechseln(char eingabeZeichen) {
      boolean istBuchstabe = Character.isLetter(eingabeZeichen);
      boolean istZiffer = Character.isDigit(eingabeZeichen);
      boolean istLeerzeichen = Character.isWhitespace(eingabeZeichen);
      boolean nichtKlein = Character.toUpperCase(eingabeZeichen) == eingabeZeichen;

      ZUSTAND neuerZustand = ZUSTAND.ungueltig;
      String fehler = null;

      if(istLeerzeichen) {
         switch(aktuellerZustand) {
            case bezirk1 : 
            case bezirk2 : 
            case bezirk3 : 
               neuerZustand = ZUSTAND.leer1; 
               break;
            case buchstabe1 : 
            case buchstabe2 : 
               neuerZustand = ZUSTAND.leer2; 
               break;
            default : 
               fehler = "Leerzeichen";
               break;
         }
      }
      else if(istBuchstabe) {
         if(! nichtKlein) {
            fehler = "Kleinbuchstabe";
         } else {
            switch(aktuellerZustand) {
               case kfz : neuerZustand = ZUSTAND.bezirk1; break;
               case bezirk1 : neuerZustand = ZUSTAND.bezirk2; break;
               case bezirk2 : neuerZustand = ZUSTAND.bezirk3; break;
               
               case leer1 : neuerZustand = ZUSTAND.buchstabe1; break;
               case buchstabe1 : neuerZustand = ZUSTAND.buchstabe2; break;
               default : 
                  fehler = "Großbuchstabe";
                  break;
            }
         }
      }
      else if(istZiffer) {
         switch(aktuellerZustand) {
            case leer2 : neuerZustand = ZUSTAND.ziffer1; break;
            case ziffer1 : neuerZustand = ZUSTAND.ziffer2; break;
            case ziffer2 : neuerZustand = ZUSTAND.ziffer3; break;
            case ziffer3 : neuerZustand = ZUSTAND.ziffer4; break;
            default : 
               fehler = "Ziffer";
         }
      }
      else{
         fehler = "Sonderzeichen";
      }

      aktuellerZustand = neuerZustand;

      return fehler;
     
   }
    
   
}