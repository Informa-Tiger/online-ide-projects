public class Test extends Group {

  public Test(int children) {
     Group lastDefaultGroup = getWorld().getDefaultGroup();
     getWorld().setDefaultGroup(this);
     
     Test child;
     if(children > 0) child = new Test(children - 1);
     new Rectangle(0, 0, 50, 50);
     if(child != null) child.move(50, 50);
     
     getWorld().setDefaultGroup(lastDefaultGroup);
  }
}

new Test(2);
