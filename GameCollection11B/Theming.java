public interface Stylable {
   public void setFillColor(CustomColor color);
   // public void setSecondaryFillColor(CustomColor color);
   public void setStrokeColor(CustomColor color);
   public void setBorderWidth(double widthInPixel);
}

public enum ColorName {
   background, foreground, primary, warn;
   
   public CustomColor toColor() {
      switch(this) {
         case background : 
            return Theming.background;
         case foreground : 
            return Theming.foreground;
         case primary : 
            return Theming.primary;
         case warn : 
            return Theming.warn;
      }
      return null;
   }
   
   public ColorName getMatchingForeground() {
      if(this == foreground) return background;
      else return foreground;
   }
   public ColorName getMatchingBackground() {
      if(this == background) return foreground;
      else return background;
   }
}

public enum Style {
   solid, framed, transparent;
}

public static class Theming {
   
   public static String fontfamily = "Candara";

   public static CustomColor background = new CustomColor(Color.black);
   public static CustomColor foreground = new CustomColor(Color.white);
   public static CustomColor primary = new CustomColor("#0d8640");
   public static CustomColor warn = new CustomColor("#ff4309");
   
   /** Returns a color between background and foregorund
    * 0 = background, 1 = foreground
    */
   public static CustomColor getBackgroundFilling(double level) {
      return foreground.fade(background, level); 
   }
   
   public static CustomColor getColor(ColorName colorName) {
      return colorName.toColor();
   }
   
   public static void style(Stylable stylable, Style style, ColorName color) {
      switch(style) {
         case solid : 
            stylable.setFillColor(color.toColor());
            stylable.setStrokeColor(color.getMatchingForeground().toColor());
            stylable.setBorderWidth(0);
            break;
         case framed : 
            stylable.setFillColor(color.getMatchingBackground().toColor());
            stylable.setStrokeColor(color.toColor());
            stylable.setBorderWidth(5);
            break;
         default : 
             
      }
   }
}

public class CustomColor {

   private int red;
   private int green;
   private int blue;

   private double alpha;

   private int hue;
   private double saturation;
   private double lightness;

   private double value;

   public static CustomColor fromRGBA(int red, int green, int blue, double alpha) {
      return new CustomColor(red, green, blue, alpha);
   }

   public static CustomColor fromRGB(int red, int green, int blue) {
      return new CustomColor(red, green, blue);
   }

   public static CustomColor fromHSLA(int hue, double saturation, double lightness, double alpha) {
      return new CustomColor(Color.fromHSLA(hue, saturation, lightness, alpha));
   }

   public static CustomColor fromHSL(int hue, double saturation, double lightness) {
      return new CustomColor(Color.fromHSL(hue, saturation, lightness));
   }


   public CustomColor(double red, double green, double blue, double alpha) {
      this.red = Math.max(0, Math.min((int) red, 255));
      this.green = Math.max(0, Math.min((int) green, 255));
      this.blue = Math.max(0, Math.min((int) blue, 255));
      this.alpha = Math.max(0.0, Math.min(alpha, 1.0));
      calculateHSL();
   }

   public CustomColor(double red, double green, double blue) {
      this(red, green, blue, 1);
   }

   public CustomColor(CustomColor customColor) {
      this(customColor.red, customColor.green, customColor.blue, customColor.alpha);
   }

   public CustomColor(String color) {
      int hex = Integer.parseInt(color.replaceAll("#", ""), 16);
      alpha = 1;
      if(color.replaceAll("#", "").length() > 6) {
         alpha = (hex % 256) / 255.0;
         hex /= 256;
      }
      red = hex / 256 / 256;
      green = (hex / 256) % 256;
      blue = hex % 256;
      calculateHSL();
   }

   public CustomColor(Color color) {
      this(color.toString());
   }

   public String toString() {
      String hex = Integer.toHex(((red * 256 + green) * 256 + blue) * 256 + (int)(alpha * 255));
      while(hex.length() < 8) hex = "0" + hex;
      return "#" + hex;
   }

   private void calculateHSL() {
      double r = red / 256.0; double g = green / 256.0; double b = blue / 256.0;
      double c_max = Math.max(r, Math.max(g, b));
      double c_min = Math.min(r, Math.min(g, b));
      double delta = c_max - c_min;

      value = c_max;
      lightness = (c_max + c_min) / 2 * 100;
      saturation = c_max == 0 ? 0 : delta / c_max * 100;
      
      double h;
      if(delta == 0) h = 0;
      else if(c_max == r) h = (g - b) / delta;
      else if(c_max == g) h = (b - r) / delta + 2;
      else if(c_max == b) h = (r - g) / delta + 4;
      hue = ((int)(60 * h)) % 360;
      
   }
   
   public int getRed() { return red; }
   public int getGreen() { return green; }
   public int getBlue() { return blue; }

   public int getHue() { return hue; }
   public double getSaturation() { return saturation; }
   public double getLightness() { return lightness; }
   public double getValue() { return value; }

   public double getAlpha() { return alpha; }


   public CustomColor copy() {
      return new CustomColor(this);
   }

   public CustomColor fade(CustomColor other, double weight) {
      return new CustomColor(
         other.red * (1 - weight) + red * weight,
         other.green * (1 - weight) + green * weight,
         other.blue * (1 - weight) + blue * weight,
         other.alpha * (1 - weight) + alpha * weight
         );
   }

   public CustomColor scaleLightness(double scale) {
      return CustomColor.fromHSLA(hue, saturation, lightness * scale, alpha);
   }
   
   public Color toBuiltinColor() {
      return new Color(red, green, blue);
   }
   
   public int toInt() {
      return 0x10000 * red + 0x100 * green + blue;
   }

   
}
