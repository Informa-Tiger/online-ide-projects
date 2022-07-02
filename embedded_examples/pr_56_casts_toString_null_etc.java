println(3 / 2d);                                         // Expected: 1.5
println(3.);                                             // Expected: 3.0
println((long)(Integer) 1);                              // Expected: 1
println(null);                                           // Expected: null
println((Rectangle) null);                               // Expected: null
println((String)null + (String)null);                    // Expected: nullnull
println(null == (Circle) null);                          // Expected: true
// double a = (Double) null;                             // Expected an error
// String s = new Rectangle(0,0,0,0); println(s);        // Expected an error
