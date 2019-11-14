# Task 5: Favorites

This task has multiple solutions. We can use SharedPreferences, Database, RemoteDatabase (such as Firebase), files in external memory...
I think a database could be a good solution and I have choosen Room because it is native and has an easy implementation with coroutines and LiveData, which is perfect for MVVM architecture.

We have to add it's dependencies in Detail and Favorites modules. ListItem is our entity. With coroutines and viewModel extension for this tool we don't worry about clear async work when viewModel is cleared. Also kotlin has to been updated and it makes us update gradle from 3.2.1 to 3.3.1 or later.

In DetailActivity we have to add some code to allow insert or remove from favorites. For Favorites module we can take List module and use it as this is a good way to keep the architecture and it's implementation. 
For ViewModel layer I choose MediatorLiveData to switch between all favorites and filtered by search sources.
