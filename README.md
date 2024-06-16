# FetchHomeworkAssignment

Introduction
--- 
Simple app that takes in a JSON from https://fetch-hiring.s3.amazonaws.com/hiring.json and displays
it in a RecyclerView. App is made following the MVVM pattern.

Building App
--- 
Download this repo and in Android Studio: File --> New --> Import Project. This hit the Play Button
to install on emulator/device. This was created with Android Studio Jellyfish 2023.3.1, if you are 
using a different version, it be necessary to change the agp version from libs.versions.toml look 
for variable "agp = "8.4.0"".

Architectural Specifics
--- 
App Icon: For a bit of fun, I added Fetch app's icon with the word "HOMEWORK" on it.

Following the MVVM pattern, we have the following files + responsibilities:

Model: OnlineDataRepository, retrieves the json data and is only used by OnlineDataViewModel.
It will first separate them by listId groups and then sort each group by name. It's main
responsibility is to return a sorted Map<Int, List<FetchItem>> where FetchItem is the JSON
Data structure and the Int is our listId. Each list of FetchItem is sorted by name in typical
lexicographic order, so "Item 4999" will come before "Item 50". The instructions didn't specify
numeric order, so I assume that are expecting lexicographic order.

View: activity_main.xml / MainActivity is our View, it takes from the ViewModel and shows what's
necessary, this is also where the RecyclerView lives. This will show a loading indicator while the
app is retrieving the JSON data. If for some reason, there is a error, the user will be greeted with
a "Error Occurred, Check Internet Connection" and a "Try Again" button which will try to load the
data
one more time. I have a separate version of this on branch "SwipeUpAndSpecificError" that uses the
swipe gesture to refresh and also show the entire error but I found that to be not as intuitive.

ViewModel: OnlineDataViewModel gets data from OnlineDataRepository and also keeps track of errors,
loading data, the actual data itself which is all contained in Flows(Kotlin's prefer
observer/subscriber
implementation).

Testing
---
This was tested using a Pixel 6a Emulator running Android 14 SDK 34 and again on a Amazon Fire
Tablet running Android 9. The main noticeable different between the two is that Android 14 has a
default splash screen showing the icon I added to this app. 