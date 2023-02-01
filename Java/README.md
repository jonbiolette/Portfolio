## Code Day Labs Internship 2022
The purpose of this application was to find random locations for the user to eat at. 
### MapsActivity:
 - MapsActivity is launched upon the user tapping the “Let’s Eat” button at the bottom of the UserInput screen, with this UserInput brings food preference, price preference, and distance willing to travel to MapsActivity.
 - MapsActivity pins all locations on the map and waits for the user to pick locations, when a pin is clicked on the user can choose to either add it to their list of liked restaurants, go to their website, or close the tab if not interested. The marker will not change color unless they add it to their list.
 - When a winner is selected the user is given the choice to go to the website or get directions via Google maps.

### TextSearch
- TextSearch’s main purpose is to return a list of businesses that meet the criteria of the user’s search
- The first step is creating the request using the users’ preferences that are brought from MapsActivity we can make a nearby search request that will return a list of nearby restaurants using the Google Maps API.
- This return a jumble of information that is processed to pull out only what we need and start to make our business object to be put into our business details array that will later be return to MapsActivity.
- I found that nearby searching only returns a select amount of information and a second search would need to happen. Using the placeID variable that is returned from the nearby search PlaceID is launched to fill the gaps that TextSearch missed
### PlaceID
- PlaceID only purpose is to get more in-depth information on the business in question. 
- PlaceID makes a similar request only this time we are making a place ID request which will return more information such as formatted phone, formatted address, business hours, website, open now, etc..
- Once PlaceID is finished and returned all information to TextSearch, the business object is returned to MapsActivity where it will be placed on the map.
- NullPointer is caught if not places meet the criteria of the search to prevent app crashes, an alert window pops up on the screen letting the user know and to restart the app
### UserInput
- UserInput activity is launched upon a successful selection of a food preference
- There are three buttons on the top of the screen that the user can pick from that will decide the amount they are willing to pay, choices include $, $$, and $$$. 
- A Seekbar at the bottom of the screen to decide the distance willing to travel. Google Maps deals distance in meters and not miles, in the background miles are being converted but could easily be changed to meters with a quick change.

Link to application [demo](https://youtu.be/2T9k_QbGCe8).


## Java Projects
- 2 prodjects from my last dedicated java programming class showcasing a binary search tree and linked lists
