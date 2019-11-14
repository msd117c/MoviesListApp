# Task 1: Wrong state

This is solved by setting the correct value to observed live data. In ListViewModel we have to set the correct value (depending on if result is null or not). This way the View layer is notified and update it's UI. 

For test task we can reuse the second test case as next change on live data would be from InProgress to SearchResult. We could also duplicate second test case and add this second onChange statement.
