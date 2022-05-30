public abstract class GameDescription {

   /** The name of the game */
   protected String name;
   /** A brief description of the game containing how to play it and how to win or lose */
   protected String description;
   /** A group of shapes to be used as image representing this game; it is expected that all objects of the image are inside (0,0)x(100,100) */
   protected Group image;
   /** Whether this game offers an multiplayer mode */
   protected boolean hasMutliplayerOption;

   public GameDescription(String name, String description, Group image, boolean hasMutliplayerOption) {
      this.name = name;
      this.description = description;
      this.image = image;
      this.hasMutliplayerOption = hasMutliplayerOption;

      image.defineCenter(0, 0);
      image.setVisible(false);
   }

   public String getName() { return name; }
   public String getDescription() { return description; }
   public boolean getHasMultiplayerOption() { return hasMutliplayerOption; }
   public Group copyImage(double x, double y, double size) {
      Group imageCopy = image.copy();
      imageCopy.setVisible(true);
      imageCopy.moveTo(x, y);
      imageCopy.scale(size / 100);
      return imageCopy;
   }

   /**
    * instatiate the described Game
    * @param multiplayerMode - whether the Game should be launched in multiplayer mode
    * @return the launched Game object+
    */
   public abstract Game instantiateGame(View returnTo, boolean multiplayerMode);

}

public abstract class Game extends View {

   public Game(View returnTo) {
      super(returnTo);
      stopActing();
   }
   
}
