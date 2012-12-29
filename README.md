CFX Image Tag in Java
=====================

This is a Java replacement for the `cfx_image` tag originally written in C++. Since the original was written in C++ and compiled into a 32-bit Windows DLL there was no replacement for those running ColdFusion on Linux servers.

Notes
-----

* This only supports a subset of actions available, specifically the resize and read actions.
* There is also limited compatibility with `cfx_jpegresize`.
