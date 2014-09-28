careless [![Build Status](https://travis-ci.org/raymanoz/careless.svg?branch=master)](https://travis-ci.org/raymanoz/careless) [ ![Download](https://api.bintray.com/packages/raymanoz/repo/careless/images/download.svg) ](https://bintray.com/raymanoz/repo/careless/_latestVersion)
=========

An in-memory less compiler, post-processor and cache

Why?
----

careless was created for fast feedback when hacking less files in your application. It allows you to keep your application 
running (usually in development mode, but could be in production), while you change your less files.

1. Change you less file, while you application is running
2. Reload your page
3. See the changes

careless will recompile the less files on-th-fly, and return css. But, because less takes a long to to compile, careless
cache's the compiled less files so recompilation will only happen if your less files have been modified since the last request.

careless also allows you to post-process the compiled css files. This is done using a list of (String) => String functions.

Also
----

You can source the less files from:
* the file system, using the LessResolvers.byFile()
* inside a jar, using LessResolvers.byJar()

For an example, see [this test](https://github.com/raymanoz/careless/blob/master/test/com/unsprung/careless/LessSourceResolversTest.java)
