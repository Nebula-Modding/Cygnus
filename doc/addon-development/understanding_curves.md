# Understanding Curves

Cygnus uses curves in a variety of places. They can be used to have more of a
fine-grained control over values in your data and resources for planets.

They can be a little difficult to understand, so I'll try to explain them
nicely.

If you've ever used CSS, you've probably seen curves before. You may have also
seen them in Blockbench's animations tab. The curves that Cygnus uses are
Bézier curves, which are the same ones as used in CSS and Blockbench.

Most of the time you'll be using *cubic* Bézier curves, which have a start
point, end point, and two control points.

When defining these points in JSON, you'll use an array of four elements with
objects representing where these points lie:

```jsonc
[
  {"x": 0.00, "y": 0}, // start point
  {"x": 0.25, "y": 0}, // first control point
  {"x": 0.50, "y": 0.25}, // second control point
  {"x": 0.00, "y": 1.00} // end point
]
```

As for what these numbers mean: they're points on a graph to draw a curve with.

When your value is used, it'll use a `t` value (typically equal to the `X`
value on the curve) and take the `Y` value of it to use as the value.

Instead of writing these curves by hand, I highly recommend using a tool such
as [cubic-bezier.com](https://cubic-bezier.com/). The start/end point are
locked to 0,0 and 1,1 respectively, but you can modify those if needed. Your
first control point is the pink circle and the second control point is the blue
one. Mess around with these values to make a curve, then you can take the X/Y
coordinates of them to make a value for your JSON.

If any of this was confusing then... whoops, sorry. You can join the Nebula
modding Discord server and ask for help if you're still confused, we'll be
happy to help :D
