# Netflex-App
Netflex – Movie Streaming App (Android + .NET API)  Netflex is a simple movie browsing app built with **Java (MVVM architecture)** on Android, connected to a **.NET backend API**. Users can explore movies, view details, and extend features like login, favorites, and reviews.


## Android App
- Java
- MVVM Architecture
- Retrofit2 (API calls)
- Gson (JSON parsing)
- LiveData & ViewModel (data binding)
- RecyclerView (list UI)
- Glide (image loading)

## Project Structure (Android)
```
app/
├── data/
│   ├── model/              # Movie, Genre, User, etc.
│   ├── network/            # ApiService, RetrofitClient
│   └── repository/         # MovieRepository handles API logic
│
├── ui/
│   ├── activity/           # MainActivity, MovieDetailActivity, LoginActivity
│   ├── viewmodel/          # ViewModels for each screen
│   └── adapter/            # RecyclerView adapters
│
├── utils/                  # Constants, SessionManager, etc.
└── res/                    # Layout XML, drawables, strings, etc.
```

## How to Use (Setup)
### Clone the repository

git clone https://github.com/thaildhe172591/Netflex-App.git

### Sample Endpoint Spec
| Endpoint       | Method | Description     |
| -------------- | ------ | --------------- |
| `/movies`      | GET    | Get all movies  |
| `/movies/{id}` | GET    | Get movie by ID |
| `/auth/login`  | POST   | Login user      |

### Example: How to Add New Feature (e.g., Movie Detail)
1. Add API call to ApiService.java

2. Call it in MovieRepository.java

3. Expose it in MovieDetailViewModel.java using LiveData

4. Observe it in MovieDetailActivity.java

5. Design layout activity_movie_detail.xml

6. Load image using Glide

### Libraries Used
| Library      | Purpose                 |
| ------------ | ----------------------- |
| Retrofit     | API requests            |
| Gson         | JSON parsing            |
| Glide        | Image loading           |
| ViewModel    | Manage UI-related data  |
| LiveData     | Observable data binding |
| RecyclerView | Display lists           |

### Research/ Learned Used
https://developer.android.com/topic/architecture
https://square.github.io/retrofit/
https://developer.android.com/topic/libraries/architecture/viewmodel
