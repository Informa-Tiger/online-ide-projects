class DeleteButton extends Rectangle {
  public DeleteButton(double x, double y) {
     super(x, y, 100, 100);
  }
  public void onMouseUp(double x, double y, int key) {
     if(getParentGroup() != null) getParentGroup().destroy();
     else destroy();
  }
}

new DeleteButton(0, 0);
new Group(new DeleteButton(200,0), new Circle(250,250, 50));
