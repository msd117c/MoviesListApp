# Bonus 3: Loading indicator

For this task we have to create another viewHolder and viewType for ListAdapter.

I clone the item layout to keep it's size and add just a progressBar. In list adapter we have to make some changes to detect when to show a loading item instead of real item. From ViewModel we can easily handle when to add a loadingItem and remove it when response is received.
