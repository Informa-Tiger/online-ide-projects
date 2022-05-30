public class TimeOut extends Actor {

   private Runnable runnable;
   private double durationInMs;
   private double timer = 0;

   public TimeOut(Runnable runnable, double durationInMs) {
      this.runnable = runnable;
      this.durationInMs = durationInMs;
      // if (durationInMs == 0) act(0);
   }

   public void act(double deltaTime) {
      timer += deltaTime;
      if(timer >= durationInMs) {
         runnable.run();
         stopActing();
         destroy();
      }
   }
   
}
