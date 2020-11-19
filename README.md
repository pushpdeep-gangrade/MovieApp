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

## API Documentation <a name="api"></a>

Signup (POST):
- URL:
'''
http://104.248.113.55:3001/v1/user/signup
'''
- Body:
'''
{
    "email":"ronmcdon@email.com",
    "password":"password"
}
'''
- Response:
'''
Signed Up Successfully
'''

Login (POST):
- URL:
'''
http://104.248.113.55:3001/v1/user/login
'''
- Body:
'''
{
    "email":"ronmcdon@email.com",
    "password":"password"
}
'''
- Response:
'''
Login Successful
'''

Actors (POST):
- URL:
'''
http://104.248.113.55:3001/v1/actors
'''
- Header:
'''
{
authorizationkey: KEY
}
'''
- Body:
'''
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
'''
- Response:
'''
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
'''

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
