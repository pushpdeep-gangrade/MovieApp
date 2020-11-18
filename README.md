# MovieApp

## Authors <a name="authors"></a>
- Pushpdeep Gangrade
- Katy Mitchell
- Valerie Ray
- Rockford Stoller

## Video Demo <a name="demo"></a>

## Project Wiki <a name="wiki"></a>

### Goals (due 11/18/2020 at 7pm)
In this assignment you are going to handle the retrieval and listing of large data in mobile app.
The goal of this  assignment is to the provide the following requirements:

- Locate a dataset that has at least 10,000 rows, with at least 3 attributes not including the id.
  The data should be imported into your server in a database.
- Create an API that provides a list of items extracted from the records stored on the server.
- Create a mobile application the enables the listing of the users in a ListView/RecyclerView. The main
  requirement is that the list should only present 50 records at any given time.
- Upon reaching the bottom of the list, the last item in the list should be labeled as "Load More",
  which when pressed should load the next 50 records.
- Upon reaching the top of the list and when the list is pulled downwards, this should load the
  previous 50 records.
- The user should be able to decide on the sorting key of the list, which is based on the attributes
  of your data and should allow the user to select ascending or descending order. When the user selects
  a sorting key the list should be reloaded and the first 50 records should be displayed based on the
  selected key.
- Your app should also provide filtering features, which should allow the user to filter the results
  based on a given attribute.
- Both sorting and filtering should trigger an API call and should be performed on the server side.
- At any given time your application should only store 100 records in memory and when any other
  records are required then an API call should be made to get the next 50 records to present. Do not
  retrieve all the 10,000+ entries, instead the API should be able to address the paging, and
  sorting requirements.
- Local Storage: The app should also use local storage using Room framework to store data in the app.
  The idea is to introduce you to using Room. The app should provide a feature where the user is able
  to store data locally, retrieve, delete and display them.

### Submission should include:

- ~~Create a Github or Bitbucket repo for the assignment.~~
- ~~Push your code to the created repo. Should contain all your code.~~
- On the same repo create a wiki page describing your api design and implementation. The wiki page
  should describe the API routes, DB Schema and all the assumptions required to provide
  authentication. In addition describe any data that is stored on the device or on the server.
- Include the Postman file in the repo.
- If you used custom APIs you should demo your API using the Postman Chrome Plugin. The API should
  be demonstrated using Postman, you should create an api component in Postman for each of your created APIs.
- ~~Demo your API using a mobile app that uses your implemented api.~~
- A 5 minute (max) screencast to demo your application.