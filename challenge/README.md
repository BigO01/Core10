## ASSUMPTIONS 
 - Assuming that the calls need to be specific i.e
   - Luke Skywalker specific API 
   - Episode 1 = films/1
 - Assuming that schema is fixed and won't change
 - Assuming ships response don't require pagination as not mentioned
 - Assuming species response don't require pagination as not mentioned
 - Assuming the species response would be unique

## GUIDE
- Start the project
- Call the specific api 
- curl --location 'localhost:8080/api/luke-starships'
- curl --location 'localhost:8080/api/total-population'
- curl --location 'localhost:8080/api/episode-1-species'