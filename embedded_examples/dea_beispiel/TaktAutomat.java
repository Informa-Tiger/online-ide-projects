public class TaktAutomat {
   private int zustand = 4;
   public boolean istViervierteltakt(String eingabe) {
      zustand = 4;
      for (int i = 0; i < eingabe.length(); i++) {
         zustandWechseln(eingabe.charAt(i));
         if(zustand < 0) break;
      }
      return zustand == 0;
   }
   public void zustandWechseln(char zeichen) {
      if(zeichen == 'g') zustand -= 4;
      else if(zeichen == 'p') zustand -= 3;
      else if(zeichen == 'h') zustand -= 2;
      else if(zeichen == 'v') zustand -= 1;
      else zustand = -1;
   }
}

TaktAutomat automat = new TaktAutomat();
println("vvvv: " + automat.istViervierteltakt("vvvv"));
println("vvvvv: " + automat.istViervierteltakt("vvvvv"));
println("g: " + automat.istViervierteltakt("g"));
println("g: " + automat.istViervierteltakt("g"));
println("vhv: " + automat.istViervierteltakt("vhv"));
println("abc: " + automat.istViervierteltakt("abc"));
