# Bonus 1: Memory leak

The memory leak is located on static queryProvider of DetailActivity. To fix it we can pass the movie ID to ViewModel as a function parameter. This way we avoid referencing DetailActivity from a ViewModel which is causing the leak.
