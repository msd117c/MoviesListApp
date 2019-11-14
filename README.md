# Task 3: Lost State

To do this task we have different options. We could, for instance, set a way to update action bar items from fragment, because this component is saving the query to keep search alive after configuration changes. However, this way could provoke more issues as we have to keep a reference to it's parent activity and fragment would lose one of it's advantages (re-usability).

I prefer to save the query on MoviesDetailActivity and after config changes restore it directly to the search view from onOptionsMenu method. 
