 /** An simple ActionListener listening for an event without params */
public interface ActionListener {
   /** Called when event is fired */
   public void onAction(ActionObject gedrueckteSchaltflaeche);
}

public interface ActionObject {
   
   public String getName();
   
}

public interface Enabelable {
   
   public void setEnabled(boolean enabled);
   
   public void updateEnabled();
   
   public boolean getEnabled();
   
}

/** A Button with a text, that can be pressed */
public class Schaltflaeche extends Group implements ActionObject, Enabelable {

   /** The text display */
   private Text text;
   /** The text to be displayed */
   private String textStr;
   /** The normal fillColor of the Button, if it isn't hovered */
   private String normalFillColor;
   /** The fillColor of the Button, if it IS hovered */
   private String hoverFillColor;
   /** An ActionListener to be called when the Button is pressed */
   private ActionListener actionlistener;
   /** The shape of the button */
   private RoundedRectangle rect;
   /** If not enabled, the button can't be clicked / hovered  */
   private boolean enabled = true;
   /** Whether button is hovered by the mouse */
   private boolean hovered;
   /** True while fiering an action, to prevent double clicks */
   private boolean fieringAction;
      
   private String name;

   public Schaltflaeche(double x, double y, double w, double h, double r, String name, String text, double fontsize, ActionListener actionlistener) {
      super();
      rect = new RoundedRectangle(x, y, w, h, r);
      this.name = name;
      this.actionlistener = actionlistener;
      this.textStr = text;
      this.text = new Text(x + w / 2, 0, fontsize, text, "Candara");
      this.text.setAlignment(Alignment.center);
      this.text.move(0, y + h / 2 - this.text.getCenterY());
      this.add(rect, this.text);
      reactToMouseEventsWhenInvisible(false);
      setFillColor(Theming.foreground);
   }
   
   public Schaltflaeche(double x, double y, double w, double h, double r, String text, double fontsize, ActionListener actionlistener) {
      this(x, y, w, h, r, text, text, fontsize, actionlistener);
   }

   public void setFillColor(Color fillcolor) {
      setFillColor(fillcolor.toString());
   }
   
   public void setFillColor(String fillcolor) {
      int hex = Integer.parseInt(fillcolor.replaceAll("#", ""), 16);
      double alpha = 1;
      if(fillcolor.replaceAll("#", "").length() > 6) {
         alpha = hex % 256;
         hex /= 256;
      }
      int red = hex / 256 / 256;
      int green = (hex / 256) % 256;
      int blue = hex % 256;
      
      this.normalFillColor = Color.fromRGBA(red, green, blue, alpha);
      setHoveredFillColor(Color.fromRGBA((int)(red * 0.7), (int)(green * 0.7), (int)(blue * 0.7), alpha));
      rect.setFillColor(fillcolor);
   }
   
   public void setHoveredFillColor(String hoveredFillColor) {
      hoverFillColor = hoveredFillColor;
   }
   
   /** Sets the fillColor of the text */
   public void setTextColor(Color textcolor) {
      text.setFillColor(textcolor);
   }
   
   /** Sets the fillColor of the text */ 
   public void setTextColor(String textcolor) {
      text.setFillColor(textcolor);
   }

   void onMouseEnter(double x, double y) {
      hovered = true;
      // if(getEnabled()) rect.setFillColor(hoverFillColor);
   }
   
   void onMouseLeave(double x, double y) {
      hovered = false;
      // if(getEnabled()) rect.setFillColor(normalFillColor);
   }
   
   void act() {
      if(getEnabled()) rect.setFillColor(hovered ? hoverFillColor : normalFillColor);
   }
   
   void onMouseDown(double x, double y, int key) {
      if(fieringAction) return;
      fieringAction = true;
      if(getEnabled() && actionlistener != null) actionlistener.onAction(this);
      fieringAction = false;
   }
   
   public String getText() {
      return textStr;
   }
   
   public void setText(String text) {
      textStr = text;
      this.text.setText(text);
   }
   
   public String getName() {
      return name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      updateEnabled();
   }
   
  	public void updateEnabled() {
    		if(getEnabled()) {
         restartActing();
      }
      else {
         stopActing();
         rect.setFillColor(normalFillColor);
      }
  	}
   
   public boolean getEnabled() {
      if (getParentGroup() instanceof Enabelable && ! ((Enabelable) getParentGroup()).getEnabled()) return false;
      return enabled;
   }
   
   public void setColorTheme(ColorTheme colorTheme) {
      switch(colorTheme) {
         case ColorTheme.transparent : 
            setFillColor(Color.fromRGBA(0, 0, 0, 0.0));
            setTextColor(Theming.foreground); 
            setHoveredFillColor(Color.fromRGBA(255, 255, 255, 0.4));
            break;
         default : 
            break;
             
      }
   }
   
}

public enum ColorTheme {
   transparent;
}

public class Dialog extends RoundedRectangle implements ActionListener {

   private TextField texts;
   private Group buttons;
   private Rectangle overlay;
   private double fontsize;
   private double padding = 10;
   private ActionListener listener;

   public Dialog(double x, double y, double w, double h, String text, int fontsize, double borderradius, String[] options, ActionListener listener) {
      super(x, y, w, h, borderradius);
      this.fontsize = fontsize;
      this.listener = listener;
      
      setFillColor(Theming.background);
      setBorderColor(Theming.foreground);
      setBorderWidth(5);
      
      texts = new TextField(text, w + padding, y + padding, w - 2 * padding, fontsize);
      setOptions(options);

      overlay = new Rectangle(0, 0, getWorld().getWidth(), getWorld().getHeight());
      overlay.setFillColor(Color.fromRGBA(0, 0, 0, 0.7));

      this.bringToFront();
      texts.bringToFront();
      buttons.bringToFront();
   }

   public Dialog(double w, double h, String text, int fontsize, String[] options, ActionListener listener) {
      this(getWorld().getWidth() / 2 - w / 2, getWorld().getHeight() / 2 - h / 2, w, h, text, fontsize, 10, options, listener);
   }

   // public void setText(String text) {
   //    if(texts != null && ! texts.isDestroyed()) texts.destroyAllChildren();
   //    if(texts == null) texts = new Group();
   //    String[] words = text.split(" ");
   //    double ypos = getCenterY() - getHeight() / 2 + padding;
   //    String buffer = "";
   //    Text line = newTextLine(ypos);
   //    for (String word : words) {
   //       String newbuffer = buffer + (buffer.isEmpty() ? "" : " ") + word;
   //       line.setText(newbuffer);
   //       
   //       if(line.getWidth() > getWidth() - 20) {
   //          line.setText(buffer);
   //          ypos += line.getHeight();
   //          buffer = word;
   //          line = newTextLine(ypos);
   //          line.setText(buffer);
   //       } else {
   //          buffer = newbuffer;
   //       }
   //    }
   // }
   
   
   // private Text newTextLine(double ypos) {
   //    Text line = new Text(getCenterX() - getWidth() / 2 + padding, ypos, fontsize, "");
   //    line.setFillColor(Theming.foreground);
   //    texts.add(line);
   //    return line;
   // }

   public void setOptions(String[] options) {
      if(buttons != null && ! texts.isDestroyed()) buttons.destroyAllChildren();
      if(buttons == null) buttons = new Group();
      Text test = new Text(0, 0, fontsize, "");
      double textheight = test.getHeight();
      test.destroy();
      double avaiblewidth = getWidth() - (options.length + 1) * padding;
      for (int i = 0; i < options.length; i++) {
         
         Schaltflaeche button = new Schaltflaeche(getCenterX() - getWidth() / 2 + padding + (avaiblewidth / options.length + padding) * i, getCenterY() + getHeight() / 2 - 3 * padding - textheight, avaiblewidth / options.length, 2 * padding + textheight, getRadius(), options[i], fontsize, this);
         button.setFillColor(Theming.foreground);
         button.setTextColor(Theming.background);
         buttons.add(button);
      }
   }
   
   
  	public void onAction(ActionObject gedrueckteSchaltflaeche) {
      texts.destroy();
      buttons.destroy();
      overlay.destroy();
      this.destroy();
      if(listener != null) listener.onAction(gedrueckteSchaltflaeche);
  	}

}


public class Menue extends CloseableView implements ActionListener {

   private Text titleDisplay;
   private Group buttons;
   private double fontsize;
   private double padding = 10;
   private ActionListener listener;
   private World world;
   private String[] options;
   private String title;

   public Menue(View returnTo, String title, int fontsize, String[] options, ActionListener listener) {
      super(returnTo);
      this.fontsize = fontsize;
      this.listener = listener;
      this.options = options;
      this.title = title;
   }
   
   public void build() {
      super.build();
    
      setOptions(options);
      
      titleDisplay = new Text(getWorld().getWidth() / 2, getWorld().getHeight() * 0.1, fontsize, title, "Candara");
      titleDisplay.setAlignment(Alignment.center);
      titleDisplay.setFillColor(Theming.foreground);
      add(titleDisplay);

      titleDisplay.bringToFront();
      buttons.bringToFront();
   }

   public void setOptions(String[] options) {
      if(buttons != null && ! buttons.isDestroyed()) buttons.destroyAllChildren();
      if(buttons == null) buttons = new Group();
      add(buttons);
      double buttonWidth = getWorld().getWidth() * 0.3;
      double spacing = 0.4;
      double buttonHeight = 0.5 * getWorld().getHeight() / (options.length + (options.length - 1) * spacing);
      for (int i = 0; i < options.length; i++) {
         Schaltflaeche button = new Schaltflaeche(getWorld().getWidth() / 2 - buttonWidth / 2, getWorld().getHeight() * 0.35 + i * buttonHeight * (1 + spacing), buttonWidth, buttonHeight, 10, options[i], fontsize * 0.6, this);
         button.setFillColor(Theming.foreground);
         button.setTextColor(Theming.background);
         buttons.add(button);
      }
   }
   
   
  	public void onAction(ActionObject gedrueckteSchaltflaeche) {
      super.onAction(gedrueckteSchaltflaeche);
      if(listener != null) listener.onAction(gedrueckteSchaltflaeche);
  	}

   public void destroy() {
      buttons.destroyAllChildren();
      titleDisplay.destroy();
      super.destroy();
   }
   
}

public class GroupWithUtilities extends Group implements Enabelable {
   
   private boolean enabled = true;
   private boolean visible = true;
   private GroupWithUtilities parent;
   
   public GroupWithUtilities() {
      super();
   }
   
   public void setVisible(boolean visible) {
      this.visible = visible;
      super.setVisible(visible);
      for (int i = 0; i < size(); i++) {
         get(i).setVisible(visible);
         // if(get(i) instanceof GroupWithUtilities)((GroupWithUtilities) get(i)).setVisible(visible);
      }
   } 
   
   public boolean isVisible() { return visible; }
   
  	public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      updateEnabled();
  	}
     
  	public void updateEnabled() {
    		for (int i = 0; i < size(); i++) {
         if(get(i) instanceof Enabelable) {
            Enabelable enabelable = (Enabelable) get(i);
            enabelable.updateEnabled();
         }
      }
  	}
   
  	public boolean getEnabled() {
      return enabled && (parent == null || parent.getEnabled());
  	}
     
}

public interface Animatable {
   
   /**
    * Sets the value of an animatable property to value
    * @returns whether an property with the given name exists
    */ 
   public boolean setAnimatableProperty(String property, double value);
   /** Returns the value of an animatable property or null if if doesn't exists */
   public Double getAnimatableProperty(String property);
   
}

public class Animation extends Actor {
   
   private Animatable animatable;
   private String property;
   private double targetValue;
   private double durationInMs;
   public double timer;
   
   public Animation(Animatable animatable, String property, double targetValue, double durationInMs) {
      super();
      this.animatable = animatable;
      this.property = property;
      this.targetValue = targetValue;
      this.durationInMs = durationInMs;
      timer = 0;
      if(durationInMs == 0) this.act(0);
   }
   
   public void act(double deltaTime) {
      timer += deltaTime;
      if(timer + deltaTime >= durationInMs) {
         animatable.setAnimatableProperty(property, targetValue);
         stop();
      } else {
         double currentValue = animatable.getAnimatableProperty(property);
         animatable.setAnimatableProperty(property, currentValue + (targetValue - currentValue) * deltaTime / (durationInMs - timer));
      }
   }
   
   public void stop() {
      if(isDestroyed()) return;
      
      destroy();
      stopActing();
   }

}

public class View extends GroupWithUtilities implements Animatable, Runnable {
   
   /** the View to return on return to */
   protected View returnTo;
   /** Overlay for fading in or out */
   protected Rectangle overlay;
   /** visibility = 1 - opacity of the overlay */
   protected double visibility;
   /** Animation duration */
   public final double DURATION = 500;
   /** view transisting to */
   private View transistTo_;
   /** whether to destroy after transition to other view */
   private boolean destroyAfterTransition;
   /** whether the View is builded completely */
   private boolean built = false;
   
   protected ArrayList<Animation> runningAnimations;
   
   public View(View returnTo) {
      super();
      setVisible(false);
      sendToBack();
      this.returnTo = returnTo;
      runningAnimations = new ArrayList<Animation>();
   }
   
   public void build() {
      built = false;
      overlay = new Rectangle(0, 0, 800, 600);
      overlay.setFillColor(0, 1);
      add(overlay);
      built = true;
   }
   
   public void buildAsync() {
      built = false;
      new TimeOut(this, 0);
   }
   
   public void removeRunningAnimations() {
      
      for (Animation animation : runningAnimations) {
         if(animation != null) animation.stop();
      }
      runningAnimations = new ArrayList<Animation>();
      
   }
   
   public void show(boolean animated) {
      removeRunningAnimations();
      if(getParentGroup() instanceof HiddenGroup) getParentGroup().remove(this);
      runningAnimations.add(new Animation(this, "visibility", 1, animated ? DURATION : 0));
      bringToFront();
   }
   
   public void hide(boolean animated) {
      removeRunningAnimations();
      setEnabled(false);
      runningAnimations.add(new Animation(this, "visibility", 0, animated ? DURATION : 0));
   }
   
  	public boolean setAnimatableProperty(String property, double value) {
      
     	switch(property) {
         case "visibility" : 
            visibility = value;
            setVisible(visibility > 0);
            setEnabled(visibility == 1);
            if(visibility == 1) restartActing();
            else stopActing();
            overlay.setFillColor(0, 1 - visibility);
            overlay.bringToFront();
            if(visibility == 1) {
               println("test");
            }
            break;
         default : 
            return false;
      }
      return true;
  	}

  	public Double getAnimatableProperty(String property) {
    		switch(property) {
         case "visibility" : 
            return visibility;
            break;
         default : 
            return null;
      }
  	}

  	public void run() {
        
      if(! built) {
         build();
         return;
      }
      
      if(transistTo_ != null) transistTo_.show(true);
      if(destroyAfterTransition) {
         removeRunningAnimations(); 
        	destroy();
      }
  	}
     
   public void transistTo(View transistTo_, boolean animated, boolean destroyAfterTransition) {
      bringToFront();
      this.transistTo_ = transistTo_;
      this.destroyAfterTransition = destroyAfterTransition;
      hide(animated);
      if(transistTo_ != null && ! transistTo_.isBuilt()) transistTo_.buildAsync();
      if(animated) new TimeOut(this, animated ? DURATION : 0);
      else {
         if(destroyAfterTransition) {
            removeRunningAnimations();
            destroy();
         }
         if(transistTo_ != null) transistTo_.show(false);
      }
   }
   
   public void return_(boolean animated, boolean destroyAfterTransition) {
      transistTo(returnTo, animated, destroyAfterTransition);
   }
   
   public boolean isBuilt() {
      return built;
   }

}

public class TextField extends GroupWithUtilities {
   
   private double x; // x-coordinate, upper left corner
   private double y; // y-coordinate, upper left corner
   private double w; // width
   private double fontsize;
   private String text;
   
   public TextField(String text, double x, double y, double w, double fontsize) {
      this.x = x; this.y = y; this.w = w; this.fontsize = fontsize; this.text = text;
      updateText();
   }
   
   public void updateText() {
      boolean wasVisible = isVisible();
      setVisible(false);
      destroyAllChildren();
      String[] words = text.split(" |(?<=\n)");
      double ypos = y;
      String buffer = "";
      Text line = newTextLine(ypos);
      for (String word : words) {
         String newbuffer = buffer + (buffer.isEmpty() ? "" : " ") + word;
         line.setText(newbuffer.replaceAll("\n", ""));
         
         if(line.getWidth() > w || buffer.endsWith("\n")) {
            line.setText(buffer);
            ypos += fontsize * 1.2; //line.getHeight();
            buffer = word;
            line = newTextLine(ypos);
            line.setText(buffer.replaceAll("\n", ""));
         } else {
            buffer = newbuffer;
         }
      }
      setVisible(wasVisible);
   }
   
   public void setText(String text) {
      this.text = text;
      updateText();
   }
   
   private Text newTextLine(double ypos) {
      Text line = new Text(x + fontsize, ypos, fontsize, "", "Candara");
      line.setFillColor(Theming.foreground);
      add(line);
      return line;
   }
   
}

public class CloseableView extends View implements ActionListener {
   
   protected Schaltflaeche returnButton;
   
   public CloseableView(View returnTo) {
      super(returnTo);
   }
   
   public void build() {
      super.build();
      
      returnButton = new Schaltflaeche(720, 20, 50, 50, 10, "return", "⨯", 50, this);
      returnButton.setFillColor("#ff4309");
      returnButton.setTextColor(Theming.foreground);
      add(returnButton);
   }
   
   public void onAction(ActionObject gedrueckteSchaltflaeche) {
     	if(gedrueckteSchaltflaeche.getName().equals("return")) {
         return_(true, true);
      }
  	}
   
}
