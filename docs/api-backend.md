This file documents the HTTP endpoints available in Netflex.WebAPI, based on the structure in Endpoints/V1.
Each endpoint is grouped by resource and version, with the corresponding HTTP method and route.

# Actors

POST /api/v1/actors -> CreateActorEndpoint
Request (multidata):
Name (string, required)
Image (IFormFile, optional)
Gender (bool, optional)
BirthDate (DateTime, optional)
Biography (string, optional)
Response:
Id (long)
GET /api/v1/actors -> GetActorsEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of ActorDto
Id, Name, Image, Gender, BirthDate, Biography
GET /api/v1/actors/{id} -> GetActorEndpoint
Request:
Id (long, path param)
Response: ActorDto
Id, Name, Image, Gender, BirthDate, Biography
PUT /api/v1/actors/{id} -> UpdateActorEndpoint
Request (multidata):
Name (string, optional)
Image (IFormFile, optional)
Gender (bool, optional)
BirthDate (DateTime, optional)
Biography (string, optional)
Response: Ok
DELETE /api/v1/actors/{id} -> DeleteActorEndpoint
Request:
Id (long, path param)
Response: Ok

# Auth

POST /api/v1/auth/signin -> SigninEndpoint
Request:
Email (string, required)
Password (string, required)
Response:
AccessToken (string)
RefreshToken (string)
POST /api/v1/auth/social-login -> SocialLoginEndpoint
Request:
Provider (string, required)
Response:
LoginUrl (string)
GET /api/v1/auth/social-login/callback -> SocialLoginCallbackEndpoint
Request:
Code (string, required)
Provider (string, required)
Response:
AccessToken (string)
RefreshToken (string)
POST /api/v1/auth/send-otp -> SendOtpEndpoint
Request:
Email (string, required)
Response: Ok
POST /api/v1/auth/verify-otp -> VerifyOtpEndpoint
Request:
Email (string, required)
Otp (string, required)
Response: Ok
POST /api/v1/auth/refresh -> RefreshEndpoint
Request:
RefreshToken (string, required)
Response:
AccessToken (string)
RefreshToken (string)
POST /api/v1/auth/reset-password -> ResetPasswordEndpoint
Request:
Email (string, required)
NewPassword (string, required)
Otp (string, required)
Response: Ok
POST /api/v1/auth/confirm-email -> ConfirmEmailEndpoint
Request:
Email (string, required)
Token (string, required)
Response: Ok
POST /api/v1/auth/logout -> LogoutEndpoint
Request:
RefreshToken (string, required)
Response: Ok
GET /api/v1/auth/me -> MeEndpoint
Request: (header: Authorization)
Response: UserDetailDto
Email, Confirmed, Roles, Permissions

# Episodes

POST /api/v1/episodes -> CreateEpisodeEndpoint
Request (multidata):
Name (string, required)
Overview (string, optional)
Video (IFormFile, optional)
Runtime (int, optional)
AirDate (DateTime, optional)
SeriesId (long, required)
Actors (list<long>, optional)
Response:
Id (long)
GET /api/v1/episodes -> GetEpisodesEndpoint
Request (query params):
Search (string, optional)
SeriesId (long, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of EpisodeDto
Id, Name, EpisodeNumber, Overview, VideoUrl, Runtime, AirDate, SeriesId, SeriesName, PosterPath
GET /api/v1/episodes/{id} -> GetEpisodeEndpoint
Request:
Id (long, path param)
Response: EpisodeDetailDto
Id, Name, EpisodeNumber, Overview, VideoUrl, Runtime, AirDate, SeriesId, SeriesName, PosterPath, Actors
PUT /api/v1/episodes/{id} -> UpdateEpisodeEndpoint
Request (multidata):
Name (string, optional)
Overview (string, optional)
Video (IFormFile, optional)
Runtime (int, optional)
AirDate (DateTime, optional)
Actors (list<long>, optional)
Response: Ok
DELETE /api/v1/episodes/{id} -> DeleteEpisodeEndpoint
Request:
Id (long, path param)
Response: Ok

# Genres

POST /api/v1/genres -> CreateGenreEndpoint
Request:
Name (string, required)
Response:
Id (long)
GET /api/v1/genres -> GetGenresEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of GenreDto
Id, Name
PUT /api/v1/genres/{id} -> UpdateGenreEndpoint
Request:
Name (string, optional)
Response: Ok
DELETE /api/v1/genres/{id} -> DeleteGenreEndpoint
Request:
Id (long, path param)
Response: Ok

# Keywords

POST /api/v1/keywords -> CreateKeywordEndpoint
Request:
Name (string, required)
Response:
Id (long)
GET /api/v1/keywords -> GetKeywordsEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of KeywordDto
Id, Name
PUT /api/v1/keywords/{id} -> UpdateKeywordEndpoint
Request:
Name (string, optional)
Response: Ok
DELETE /api/v1/keywords/{id} -> DeleteKeywordEndpoint
Request:
Id (long, path param)
Response: Ok

# Movies

POST /api/v1/movies -> CreateMovieEndpoint
Request (multidata):
Title (string, required)
Overview (string, optional)
Poster (IFormFile, optional)
Backdrop (IFormFile, optional)
Video (IFormFile, optional)
CountryIso (string, optional)
Runtime (int, optional)
ReleaseDate (DateTime, optional)
Actors (list<long>, optional)
Keywords (list<long>, optional)
Genres (list<long>, optional)
Response:
Id (long)
GET /api/v1/movies -> GetMoviesEndpoint
Request (query params):
Search (string, optional)
Genres (string, optional, comma-separated)
Keywords (string, optional, comma-separated)
Actors (string, optional, comma-separated)
Country (string, optional)
Year (int, optional)
SortBy (string, optional)
FollowerId (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of MovieDto
Id, Title, Overview, PosterPath, BackdropPath, VideoUrl, CountryIso, Runtime, ReleaseDate
GET /api/v1/movies/{id} -> GetMovieEndpoint
Request:
Id (long, path param)
Response: MovieDetailDto
Id, Title, Overview, PosterPath, BackdropPath, VideoUrl, CountryIso, Runtime, ReleaseDate, AverageRating, TotalReviews, Actors, Keywords, Genres
PUT /api/v1/movies/{id} -> UpdateMovieEndpoint
Request (multidata):
Title (string, optional)
Overview (string, optional)
Poster (IFormFile, optional)
Backdrop (IFormFile, optional)
Video (IFormFile, optional)
CountryIso (string, optional)
Runtime (int, optional)
ReleaseDate (DateTime, optional)
Actors (list<long>, optional)
Keywords (list<long>, optional)
Genres (list<long>, optional)
Response: Ok
DELETE /api/v1/movies/{id} -> DeleteMovieEndpoint
Request:
Id (long, path param)
Response: Ok

# Notifications

POST /api/v1/notifications -> CreateNotificationEndpoint
Request:
Title (string, required)
Content (string, required)
Response:
Id (long)
GET /api/v1/notifications -> GetNotificationsEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of NotificationDto
Id, Title, Content, CreatedAt
DELETE /api/v1/notifications/{id} -> DeleteNotificationEndpoint
Request:
Id (long, path param)
Response: Ok

# Reports

GET /api/v1/reports -> GetReportsEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of ReportDto
Id, Reason, Description, Process
PUT /api/v1/reports/{id} -> UpdateReportEndpoint
Request:
Reason (string, optional)
Description (string, optional)
Response: Ok
DELETE /api/v1/reports/{id} -> DeleteReportEndpoint
Request:
Id (long, path param)
Response: Ok

# Series

POST /api/v1/series -> CreateSerieEndpoint
Request (multidata):
Name (string, required)
Overview (string, optional)
Poster (IFormFile, optional)
Backdrop (IFormFile, optional)
CountryIso (string, optional)
FirstAirDate (DateTime, optional)
LastAirDate (DateTime, optional)
Keywords (list<long>, optional)
Genres (list<long>, optional)
Response:
Id (long)
GET /api/v1/series -> GetSeriesEndpoint
Request (query params):
Search (string, optional)
Genres (string, optional, comma-separated)
Keywords (string, optional, comma-separated)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of SerieDto
Id, Name, Overview, PosterPath, BackdropPath, CountryIso, FirstAirDate, LastAirDate
GET /api/v1/series/{id} -> GetSerieEndpoint
Request:
Id (long, path param)
Response: SerieDetailDto
Id, Name, Overview, PosterPath, BackdropPath, CountryIso, FirstAirDate, LastAirDate, Keywords, Genres
PUT /api/v1/series/{id} -> UpdateSerieEndpoint
Request (multidata):
Name (string, optional)
Overview (string, optional)
Poster (IFormFile, optional)
Backdrop (IFormFile, optional)
CountryIso (string, optional)
FirstAirDate (DateTime, optional)
LastAirDate (DateTime, optional)
Keywords (list<long>, optional)
Genres (list<long>, optional)
Response: Ok
DELETE /api/v1/series/{id} -> DeleteSerieEndpoint
Request:
Id (long, path param)
Response: Ok

# Users

POST /api/v1/users -> CreateUserEndpoint
Request:
Email (string, required)
Password (string, required)
Response:
Id (long)
GET /api/v1/users -> GetUsersEndpoint
Request (query params):
Search (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of UserDetailDto
Email, Confirmed, Roles, Permissions
PUT /api/v1/users/{id} -> UpdateUserEndpoint
Request:
Email (string, optional)
Password (string, optional)
Response: Ok
POST /api/v1/users/change-password -> ChangePasswordEndpoint
Request:
OldPassword (string, required)
NewPassword (string, required)
Response: Ok
POST /api/v1/users/follow -> FollowEndpoint
Request:
TargetId (string, required)
TargetType (string, required)
Response: Ok
POST /api/v1/users/unfollow -> UnfollowEndpoint
Request:
TargetId (string, required)
TargetType (string, required)
Response: Ok
POST /api/v1/users/report -> ReportEndpoint
Request:
Reason (string, required)
Description (string, optional)
Response: Ok
POST /api/v1/users/review -> ReviewEndpoint
Request:
UserId (string, required)
TargetId (string, required)
TargetType (string, required)
Rating (int, required)
Response: Ok
GET /api/v1/users/notifications -> GetNotificationsEndpoint
Request (query params):
Search (string, optional)
SortBy (string, optional)
PageIndex (int, default 1)
PageSize (int, default 10)
Response: List of NotificationDto
Id, Title, Content, CreatedAt
POST /api/v1/users/notifications/read -> MarkReadNotificationEndpoint
Request:
NotificationId (long, required)
Response: Ok
POST /api/v1/users/notifications/readall -> MarkReadAllNotificationEndpoint
Request: none
Response: Ok
