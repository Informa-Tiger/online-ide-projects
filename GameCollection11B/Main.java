// SystemTools.setSpeed(-1);
Rectangle background = new Rectangle(0, 0, 800, 600);
background.setFillColor(Theming.background);

HiddenGroup hidden = new HiddenGroup();
hidden.setVisible(false);
hidden.getWorld().setDefaultGroup(hidden);

MainMenu mainMenu = new MainMenu();
mainMenu.build();
mainMenu.show(true);

class HiddenGroup extends Group { }
