public class BallGame extends Game {

   private Circle ball;
   
   public BallGame(View returnTo) {
      super(returnTo);
   }

   public void build() {
      super.build();
      add(overlay);
      ball = new Circle(100, 100, 20);
      ball.setFillColor(Color.blueviolet);
      add(ball);
   }

   public void act() {
      ball.move(-2, -2);
      if(ball.isOutsideView()) {
         return_(true, true);
      }
   }

}
