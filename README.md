# FetchHomeworkAssignment

Introduction
--- 
Simple app that takes in a JSON from https://fetch-hiring.s3.amazonaws.com/hiring.json and displays
it in a RecyclerView. App is made following the MVVM pattern.

Specifics
--- 
Following the MVVM pattern, we have the following files + responsibilities:

Model: OnlineDataRepository, retrieves the json data and is only used by OnlineDataViewModel.
It will first separate them by listId groups and then sort each group by name. It's main 
responsibility is to return a Map<Int, List<FetchItem>> where FetchItem is the JSON
Data structure and the Int is our listId. 

View: activity_main.xml / MainActivity is our View, it takes from the ViewModel and shows what's
necessary, this is also where the RecyclerView lives. I added a pull down ability to refresh the
list, I found it useful when I ran into a network issue. I also print the error on this screen if
there was some error getting the network data

ViewModel: OnlineDataViewModel gets data from OnlineDataRepository and also keeps track of errors,
loading data, the actual data itself which is all contained in Flows(Kotlin's prefer observer/subscriber
implementation). 

Testing
---
This was tested using a Pixel 6a Emulator running Android 14 SDK 34 and again on a Amazon Fire 
Tablet running Android 9. The main noticeable different between the two is that Android 14 has a
default splash screen showing the icon I added to this app. 