### Ivan de Wergifosse 20091388

### Final Project

# Geosite

#### App purpose:

This application was designed as the final project of the Higher Diploma in Computer Science from Waterford IT
to assist surveyors of Geological Survey Ireland (GSI) locate and assess potential borehole drill sites.

To conduct a survey, a geologist from GSI will visit an area of interest or study and attempt to locate appropriate sites to conduct a drilling survey.
Once an appropriate site has been located, contact must be made with the landowner of the site so as to request permission for the survey.
If the landowner agrees to the survey, their details and the location of the proposed borehole are to be recorded using this application.

#### App Use:

To use the app, the geologist must log in (using a pre-approved login, as sign up is not available through the app). 
Upon login, the geologist is presented with a list of all geosites recorded to date. They can add a new site either using 
the floating action button in the bottom right hand corner of the view or via the Add option from the menu in the top right hand corner of the view.

Selecting either of these options will present the geologist with a form where they can record all required details, including the name of the
landowner, their phone number, the title of the site for reference, the location of the proposed borehole, and any other details or comments that
may be pertinent. There is also an option to add an image of the site, though this image will be chosen from files existing on the device and does
no attempt to access the camera. The location can be seen in this view in two ways, the latitude and longitude is shown in the top right while a map
representation is shown in the bottom of the screen. Tapping this map view will open a new map view which can be used to adjust the marker by 
holding the marker and dragging it to the desired location. Tapping on the marker will also bring up the options to get direction to it via Google
Maps, which is useful for revisiting sites. Pressing back will return the user to the form and hitting Save will save the site while cancel will
discard the entry.

From the list view, tapping on any of the recorded sites will bring the user to the same form as to add a geosite but in this case the form will be
populated with the details of the site. An option to delete the site is also added to the menu bar of the edit site view. Save will update the site
details with any edits made.

From the menu in the list view, there is an option to Show Map, which brings the user to a map view containing all the geosites recorded within the
database. Tapping on any of these sites will show their title, and bring up options to get directions to the selected site via Google Maps.

Logout will log out the user and clear the Geosite List, which will be repopulated when a user logs back in.

Drillers can then revisit sites and coordinate with landowners for visits. There is a drop down menu that the driller can then use to indicate whether
the site will be suitable or not.

#### Setup

Within the GitHub repo for this application you can find almost all the code required to run this application, though some dependencies are missing as
these will be unique to whoever is setting up the application. To get started, an API Key must be obtained for Google Maps to work in the app. This
can be obtained by following the instructions outlined here: https://developers.google.com/maps/documentation/android-sdk/start#get-key

This key will need to be entered either directly into the AndroidMaifest within the metadata tag (in place of the @string/google_maps_key) or create
a google_maps_api.xml resource file.

Secondly, a Firebase account must be created at firebase.google.com , create a project and activate Authentication (Email and Password), Realtime Database
and Storage (ensuring that the rules applied to these allow for both read and write access) and then download the google-services.json file and place it in
the app folder of the project. Firebase can then be linked in the app using the built in Firebase options in Android Studio Tools. Lastly, one line of code
may need to be changed, line 69 within the GeositeFireStore model contains a link, please ensure that this link matches that shown at the top of your Realtime
Database.

### Presentation Slides

<iframe src="https://onedrive.live.com/embed?cid=893F6A1E1B1EB7F7&amp;resid=893F6A1E1B1EB7F7%217553&amp;authkey=AG6-5qOflM8537g&amp;em=2&amp;wdAr=1.7777777777777777" width="476px" height="288px" frameborder="0">This is an embedded <a target="_blank" href="https://office.com">Microsoft Office</a> presentation, powered by <a target="_blank" href="https://office.com/webapps">Office</a>.</iframe>
