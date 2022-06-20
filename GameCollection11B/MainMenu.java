public class MainMenu extends View implements ActionListener {
   
   private ArrayList<GameCard> gameCards;
   private Schaltflaeche left;
   private Schaltflaeche right;
   private Rectangle background;
   private int currentGame;
   private Text title;

   public MainMenu() {
      super(null);
   }

   /**
    * Inits the menu (draws the graphics, ...)
    * To be called by a Game after it finished
    */
   public void build() {
      super.build();
      background = new Rectangle(0, 0, 800, 600);
      background.setFillColor(Theming.background);
      this.add(background);
      
      gameCards = new ArrayList<GameCard>();
      gameCards.add(new GameCard("0", new TestGameDescription("Game 1", "Bla", Color.darkmagenta), this));
      gameCards.add(new GameCard("1", new TestGameDescription("Game 2", "Bla", Color.blue), this));
      gameCards.add(new GameCard("2", new TestGameDescription("Game 3", "Bla", Color.coral), this));
      gameCards.add(new GameCard("3", new TestGameDescription("Game 4", "Bla", Color.darkgreen), this));
      gameCards.add(new GameCard("4", new TestGameDescription("Game 5", "Bla", Color.dodgerblue), this));
      for (GameCard gameCard : gameCards) {
         this.add(gameCard);
         gameCard.setVisible(true);
      }
      scrollToGame(0, false);
      
      left = new Schaltflaeche(0, 0, 200, 600, 0, "left", "", 0, this);
      right = new Schaltflaeche(800 - 200, 0, 270, 600, 0, "right", "", 0, this);
      this.add(left, right);
      left.sendToBack();
      right.sendToBack();

      title = new Text(400, 40, 64, "Spieleauswahl");
      title.setAlignment(Alignment.center);
      add(title);
   }
   
   void onKeyTyped(String key) {
      if(key == Key.ArrowLeft) {
         scroll(true);
      }
      if(key == Key.ArrowRight) {
         scroll(false);
      }
   }
   
   void scroll(boolean left) {
      scrollToGame((currentGame + (left ? -1 : 1) + gameCards.size()) % gameCards.size(), true);
   }
   
   void scrollToGame(int gameTo, boolean animated) {
      currentGame = gameTo;
      for (int i = 0; i < gameCards.size(); i++) {
         if(i == (currentGame - 1 + gameCards.size()) % gameCards.size()) {
            gameCards.get(i).setState(-1, animated);
         } else if(i == currentGame) {
            gameCards.get(i).setState(0, animated);
         } else if(i == (currentGame + 1) % gameCards.size()) {
            gameCards.get(i).setState(1, animated);
         } else {
            gameCards.get(i).setState(-2, animated);
         }
         // gameCards.get(i).setState(currentGame, true);
      }
   
   }
  	public void onAction(ActionObject gedrueckteSchaltflaeche) {
		//TODO: Methode füllen
      if(gedrueckteSchaltflaeche.getName().equals("left")) {
         scroll(true);
      }
      else if(gedrueckteSchaltflaeche.getName().equals("right")) {
         scroll(false);
      } else {
         int number = Integer.parseInt(gedrueckteSchaltflaeche.getName());
         if(! Double.isNaN(number)) {
            GameDescription game = gameCards.get(number).getGame();
            transistTo(new GameView(this, game), true, false);
         }
      }
  	}

}

public class GameCard extends GroupWithUtilities implements ActionListener, Animatable, Runnable {

   private ActionListener listener;
   private double fontsize;
   private Text titleDisplay;
   private Schaltflaeche playButton;
   private int state = 0; // -2: outside view, -1: left, 0: center, 1: right
   private RoundedRectangle frame;

   private double SCALE = 2.0 / 3.0;
   private double UNIT = 8;
   private double WIDTH = 30 * UNIT;
   private double HEIGHT = 40 * UNIT;

   private int DURATION = 500;
   private double scale = 1;
   private double luminance = 100;
   private Animation[] runningAnimations = new Animation[4];
   private double y;
   
   private GameDescription game;

   public GameCard(double y, double w, double h, String name, GameDescription game, int fontsize, double borderradius, ActionListener listener) {
      setVisible(false);
      
      this.listener = listener;
      fontsize = (int)(fontsize * (UNIT / 10));
      this.fontsize = fontsize;
      this.game = game;
      double x = 0;
      this.y = y;
      println(y);

      frame = new RoundedRectangle(x, 0, w, h, borderradius);
      frame.setFillColor(new Color(65, 65, 65));
      // frame.setBorderColor(Theming.foreground);
      // frame.setBorderWidth(0);
      add(frame);
      
      titleDisplay = new Text(x + w / 2, 0 + 1 * UNIT, fontsize * 1.1, game.getName(), "Candara");
      titleDisplay.setAlignment(Alignment.center);
      titleDisplay.setFillColor(Theming.foreground);
      add(titleDisplay);

      RoundedRectangle imageFrame = new RoundedRectangle(x + 2 * UNIT, 0 + fontsize + 4 * UNIT, w - 4 * UNIT, h / 2, 1 * UNIT);
      imageFrame.setFillColor(new Color(43, 43, 43));
      add(imageFrame);

      Group image = game.copyImage(x + 3 * UNIT + (w - 4 * UNIT - h / 2) / 2, 0 + fontsize + 5 * UNIT, h / 2 - 2 * UNIT);
      add(image);

      playButton = new Schaltflaeche(x + 2 * UNIT, 0 + fontsize + 6 * UNIT + h / 2, w - 4 * UNIT, h * 0.2, 1 * UNIT, name, "Spielen", 32, this);
      playButton.setFillColor(Theming.foreground);
      playButton.setTextColor(Theming.background);
      add(playButton);

      defineCenter(WIDTH / 2, HEIGHT / 2);
      moveTo(getWorld().getWidth() / 2, y);
      
      setState(0, false);
   }
   

   public void setState(int state, boolean animated) {
      switch(state) {
         case -2 : 
            if(this.state != -2) goOutsideView(this.state == -1, animated);
            break;
         case -1 : 
            if(this.state == -2) goOutsideView(true, false);
            animate(getWorld().getWidth() / 2 - WIDTH / 2 - SCALE * WIDTH / 2 - 100, y, SCALE, 50, animated);
            break;
         case 0 : 
            if(this.state != 0) {
               animate(getWorld().getWidth() / 2, y, 1, 100, animated);
            }
            break;
            
         case 1 : 
            if(this.state == -2) goOutsideView(false, false);
            animate(getWorld().getWidth() / 2 + WIDTH / 2 + SCALE * WIDTH / 2 + 100, y, SCALE, 50, animated);
            break;

         default : 
            System.exit(1);
             
      }
      if(state != 0) playButton.setEnabled(false);
      else new TimeOut(this, DURATION);
      this.state = state;
   }
   
   public void goOutsideView(boolean left, boolean animated) {
      if(left) animate(- SCALE * SCALE * WIDTH / 2 - 50, y, SCALE * SCALE, 20, animated);
      else animate(getWorld().getWidth() + SCALE * SCALE * WIDTH + 50, y, SCALE * SCALE, 20, animated);
   }
   
   public void animate(double xTo, double yTo, double scaleTo_, double luminanceTo, boolean animated) {
      for (Animation animation : runningAnimations) {
         if(animation != null) animation.stop();
         animation = null;
      }
      double duration = animated ? DURATION : 0;
      runningAnimations[0] = new Animation(this, "x", xTo, duration);
      runningAnimations[1] = new Animation(this, "y", yTo, duration);
      runningAnimations[2] = new Animation(this, "scale", scaleTo_, duration);
      runningAnimations[3] = new Animation(this, "luminance", luminanceTo, duration);
   }
   
   public void scaleTo(double scaleTo) {
      scale(scaleTo / scale);
      scale = scaleTo;
   }

   public GameCard(String name, GameDescription game, ActionListener listener) {
      GameCard(getWorld().getHeight() / 2, WIDTH, HEIGHT, name, game, 40, 10, listener);
   }
   
   
   public int getState() { return state; }

  	public void onAction(ActionObject gedrueckteSchaltflaeche) {
    		if(state == 0) listener.onAction(gedrueckteSchaltflaeche);
  	}
     
   public GameDescription getGame() {
      return game;
   }

  	public boolean setAnimatableProperty(String property, double value) {
     	switch(property) {
         case "x" : 
            moveTo(value, getCenterY());
            break;
         case "y" : 
            moveTo(getCenterX(), value);
            break;
         case "scale" : 
            scaleTo(value);
            break;
         case "luminance" : 
            luminance = value;
            tint(Color.fromHSL(0, 0, value));
            break;
         default : 
            return false;
      }
      return true;
  	}

  	public Double getAnimatableProperty(String property) {
    		switch(property) {
         case "x" : 
            return getCenterX();
         case "y" : 
            return getCenterY();
         case "scale" : 
            return scale;
         case "luminance" : 
            return luminance;
         default : 
            return null;
      }
  	}

  	public void run() {
    		playButton.setEnabled(state == 0);
  	}

}

public class GameView extends CloseableView implements ActionListener {
   
   private GameDescription game;
   private Text title;
   private TextField description;
   private Schaltflaeche playButton;
   private Rectangle background;
   
   public GameView(View returnTo, GameDescription game) {
     	super(returnTo);
      this.game = game;
   }

   public void build() {
      super.build();
      // background = new Rectangle(0, 0, 800, 600);
      // background.setFillColor("#1e1e1e");
      // add(background);

      title = new Text(400, 30, 64, game.getName(), "Candara");
      title.setFillColor(Theming.foreground);
      title.setAlignment(Alignment.center);
      add(title);

      description = new TextField(game.getDescription(), 25, 140, 430, 22);
      add(description);

      RoundedRectangle imageFrame = new RoundedRectangle(525, 150 + 20, 250, 200, 10);
      imageFrame.setFillColor(new Color(43, 43, 43));
      add(imageFrame);

      Group image = game.copyImage(535 + 230 / 2 - 180 / 2, 160 + 20, 180);
      add(image);

      playButton = new Schaltflaeche(525, 400 + 20, 250, 80, 10, "play", "Spielen", 40, this);
      playButton.setFillColor(Theming.primary);//"#0d8640");
      playButton.setTextColor(Theming.foreground);
      add(playButton);

      add(overlay);
      overlay.setVisible(true);
      overlay.bringToFront();
   }

   public void onAction(ActionObject gedrueckteSchaltflaeche) {
      super.onAction(gedrueckteSchaltflaeche);
      if(gedrueckteSchaltflaeche.getName().equals("play")) {
         transistTo(game.instantiateGame(returnTo, false), true, true);
      }
   }
     
}
