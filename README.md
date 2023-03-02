# sampleFlatRock


## packages

root: App's entry points and the related ViewModels
app: Android framework specific classes, apart from the app entry points (Fragment, Activity, Application)
db: classes related to the database. Database entity classes are also kept here (with *Raw suffix)
di: dependency injection modules
model: domain data classes, use case classes, and rich domain objects
net: all the classes used to fetch data from the internet, including data classes used to parse Json files
repo: repository layer. Mediates data request to the net layer or db layer


## fragments


ItemDetailFragment: shows the details of one book
ItemListFragment: it has a search field with which books can be searched by title, author, or anything else the
backend is capable of. The results are stored locally in case of network outage.
NetworkSateFragment: listens to network events and shows status messages (error, refreshing, nothing) accordingly
SavedListFragment: lists the books the user has previously saved.


## process

The usual way to retrieve data is:

1. the user initiates a data fetch (this can be automatic)
2. the ui (fragment) calls a UseCase
3. UseCase calls Repository
4. Repository calls the network and puts the result to the model (the DB)
5. the ui (fragment) listens to LiveData stored in the db and displays it if something changes

