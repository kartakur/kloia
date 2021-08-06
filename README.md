# kloia
Demo project for kloia
 - Article url --> http://localhost:9092/
 - Review url --> http://localhost:9091/
 - Health check --> GET <Review/Article_url>/actuator/health

  * POST /articles  --  Save posted article entity to DB
  * PUT /articles/{articleId}  --  Update article entity by ID with given artibutes
  * DELETE /articles/{articleId}  --  Delete article by ID
  * GET /articles/{articleId}  --  Get article by ID
  * GET /articles  --  Get all articles
  * GET /articles?title=John&starCount=6  --  Get article entity by given filter. Search by "like" of given filter
  * POST /reviews  --  Save posted review entity to DB
  * PUT /reviews/{reviewId}  --  Update review entity by ID with given artibutes
  * DELETE /reviews/{reviewId}  --  Delete review by ID
  * GET /reviews/{reviewId}  --  Get review by ID
  * GET /reviews  --  Get all review
  * GET /reviews?reviewer=Doe&reviewCount=7&articleId=100  --  Get review entity by given filter. Search by "like" of given filter


# Instructions

 * First create docker network
   - docker network create --driver bridge --subnet=148.14.72.0/24 kloia-local
 * Go to dockerSetup directory from command prompt
 * If you have Gıt Bash or any other way to run bash script on Windows
   - ./kloiaSetup.sh -a up -km
 * If not simply run kloiaSetup.bat
   - kloiaSetup.bat
