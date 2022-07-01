Rectangle r = new Rectangle(0, 0, 100, 100);
Group g = new Group(r);
Group defaultGroup = new Group();
defaultGroup.getWorld().setDefaultGroup(defaultGroup);
Group copy = g.copy();
g.destroy();
copy.destroy();
println(defaultGroup.size());
