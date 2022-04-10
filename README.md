### Ivan de Wergifosse 20091388

### Mobile App Assignment 02

# Geosite

#### App Use:

This is an application for logging sites of interest to the Geological Heritage project of Geological
Survey Ireland.

This app allows for the login of contributors, authenticated though Firebase authentication methods.
Login is also possible using a Google account.

Upon sign in, contributors will see a full list of all sites they have logged to date, stored in a realtime
database using Firebase. Information displayed on this list includes the title of the site, a brief
description and the associated Irish Geologial Heritage Theme. If an image has been added this image
will also be displayed on this card.

An alternative way to view logged geosites is through the Show Map option, which can be access through
the menu in the top right hand corner of the site list view. This presents a map view showing all logged
sites as markers. These can be tapped to display their title, description and image.

To log a new geosite, either select the Add option from the aforementioned menu, or by tapping the
floating action button in the bottom right of the screen. This bring the contributor to the add geosite
view where they can add a title, description, select an IGH Theme from the drop down menu, add an image
from the internal storage, and add the location of the geosite. By default the location will be set 
to the location of the user, though this can be adjusted by selecting the map and dragging the marker
to the desired location. Tapping Save will add the geosite to the database, and any image added will 
be added to a Firebase storage.

Geosites can be edited by tapping on the site the user wishes to edit. This will present a view
identical to the add geosite view but already populated with details already entered, save for the
IGH Theme, which will default to the first on the list. If saving edits, please ensure the correct
IGH Theme is selected.

To log out of the app simply select the Logout option from the menu at the top of the list view.

#### Technology

The structure for this application is similar the Placemark lab from the Mobile App Development module
of the Computer Science HDip from Waterford IT, using a Model-View-Presenter structure and developed
using Kotlin. Some aspects incorporated are from the DonationX lab of the same module.

Transition effects are incorporated using a guide found here: 
https://www.youtube.com/watch?v=0s6x3Sn4eYo&ab_channel=CodinginFlow