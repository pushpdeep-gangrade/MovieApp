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

<<<<<<< HEAD

### Backend Design
- The 'Actor' API provides a list of items extracted from the records stored on the server.
- Sorting and filtering trigger an API call which is performed on the server side.
- At any given time the app only stores 100 records in memory. When any other
  records are required then an API call is made to get the next 50 records to present.
  Instead of retrieving all the 10,000+ entries, the API addresses the paging and sorting requirements.
- Local Storage: The app uses local storage via Room framework to store data. The app provides
  a feature where the user is able to store data locally, retrieve, delete and display them.
=======
## API Documentation <a name="api"></a>

Signup (POST):
- URL:
```
http://104.248.113.55:3001/v1/user/signup
```
- Body:
```
{
    "email":"ronmcdon@email.com",
    "password":"password"
}
```
- Response:
```
Signed Up Successfully
```

Login (POST):
- URL:
```
http://104.248.113.55:3001/v1/user/login
```
- Body:
```
{
    "email":"ronmcdon@email.com",
    "password":"password"
}
```
- Response:
```
Login Successful
```

Actors (POST):
- URL:
```
http://104.248.113.55:3001/v1/actors
```
- Header:
```
{
authorizationkey: KEY
}
```
- Body:
```
{
    "skips":"0",
    "limit":"50",
    "filters": "{
                "$and": [
                  {
                  "birthYear": {"$gte": 1950}
                  }
                ]
            }",
    "sortMethod":"{
                  "primaryName": 1
                }"
}
```
- Response:
```
[
    {
        "_id": "5fadd6a7091ac1be5d9ce679",
        "nconst": "nm0007289",
        "primaryName": "A-Trak",
        "birthYear": 1982,
        "deathYear": "\\N",
        "primaryProfession": "soundtrack,actor",
        "knownForTitles": "tt2294449,tt1674771,tt5164432,tt1636826"
    },
    {
        "_id": "5fadd727091ac1be5d9dd7b0",
        "nconst": "nm0072200",
        "primaryName": "A. Jonathan Benny",
        "birthYear": 1970,
        "deathYear": "\\N",
        "primaryProfession": "camera_department,cinematographer,assistant_director",
        "knownForTitles": "tt4878326,tt5689068,tt6106704,tt5292622"
    },
    ...........
]
```

### Submission should include:

- ~~Create a Github or Bitbucket repo for the assignment.~~
- ~~Push your code to the created repo. Should contain all your code.~~
- ~~On the same repo create a wiki page describing your api design and implementation. The wiki page
  should describe the API routes, DB Schema and all the assumptions required to provide
  authentication. In addition describe any data that is stored on the device or on the server.
- ~~Include the Postman file in the repo.
- ~~If you used custom APIs you should demo your API using the Postman Chrome Plugin. The API should
  be demonstrated using Postman, you should create an api component in Postman for each of your created APIs.
- ~~Demo your API using a mobile app that uses your implemented api.~~
- A 5 minute (max) screencast to demo your application.
>>>>>>> 221c9b7f93152bda5b76a944b1b042000f54a8a0
