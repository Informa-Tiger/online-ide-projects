Autokennzeichen kennzeichen = new Autokennzeichen();
String aufdruck = "TES T 1";

if(kennzeichen.istKennzeichenGueltig(aufdruck)) {
   Text schild = new Text(30, 30, 40, aufdruck);
   schild.setFillColor("white");
}
else {
   Text fehler = new Text(30, 30, 40, "UNGÜLTIG!");
   fehler.setFillColor("red");
}
