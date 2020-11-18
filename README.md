# MovieApp

## Authors <a name="authors"></a>
- Pushpdeep Gangrade
- Katy Mitchell
- Valerie Ray
- Rockford Stoller

## Video Demo <a name="demo"></a>
https://youtu.be/BbX7ewd0OV8

## Project Wiki <a name="wiki"></a>

This assignment handles the retrieval and listing of large data in mobile apps.
The movie app implements these features:
- A dataset with at least 10,000 rows was imported into our server in a database. Attributes include
  each person's name, profession, birth and death year.
- The app enables the listing of the users in a ListView/RecyclerView. The list only presents
  50 records at any given time.
- Upon reaching the bottom of the list, the last item in the list is labeled as "Load More",
  which when pressed loads the next 50 records.
- Upon reaching the top of the list and when the list is pulled downwards, the previous 50 records
  are loaded
- The user can decide on the sorting key of the list, which is based on data attributes. The user can
  select ascending or descending order. When the user selects a sorting key the list is reloaded and
  the first 50 records are displayed based on the selected key.
- Filtering feature: The user can filter the results based on a given attribute.


### Backend Design
- The 'Actor' API provides a list of items extracted from the records stored on the server.
- Sorting and filtering trigger an API call which is performed on the server side.
- At any given time the app only stores 100 records in memory. When any other
  records are required then an API call is made to get the next 50 records to present.
  Instead of retrieving all the 10,000+ entries, the API addresses the paging and sorting requirements.
- Local Storage: The app uses local storage via Room framework to store data. The app provides
  a feature where the user is able to store data locally, retrieve, delete and display them.
