println(3 / 2d);                                         // Expected: 1.5
println(3.);                                             // Expected: 3.0
println(null);                                           // Expected: null
println((String)null + (String)null);                    // Expected: nullnull
println(null == new Circle(0, 0, 0).getParentGroup());   // Expected: true
double a = (Double) null;                                // Expected an error
