public class TestGameDescription extends GameDescription {

   public static final String BLINDTEXT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

   public TestGameDescription(String name, String description, Color color) {
      super(name, BLINDTEXT, makeImage(color), false);
   }
   // public TestGameDescription() {
   //    super("TestGame", "This is a test Game.\nYou can do nothing in it.", makeImage(), false);
   // }

   private Group makeImage(Color color) {
      image = new Group();
      Circle c = new Circle(50, 50, 50);
      c.setFillColor(color);
      image.add(c);
      return image;
   }
   
  	public Game instantiateGame(View returnTo, boolean multiplayerMode) {
    	return new BallGame(returnTo);
  	}

}
