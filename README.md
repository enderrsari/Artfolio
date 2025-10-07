# Artfolio

An Android application for art enthusiasts to share and discover artwork.

## Features

- User authentication (sign up/in)
- Upload artwork with images
- Browse artwork gallery
- View detailed artwork information

## Architecture

This project follows the MVVM (Model-View-ViewModel) architecture pattern with a clean separation of concerns:

```
com.ender.artfolio
├── data
│   ├── model       # Data models
│   └── repository  # Data repositories
├── features
│   └── arts        # Feature-specific ViewModels
├── ui
│   ├── activities  # Android Activities
│   ├── adapters    # RecyclerView adapters
│   └── fragments   # Android Fragments
└── util            # Utility classes
```

## Security

Sensitive information such as Firebase configuration is excluded from version control through `.gitignore`.

## Setup

1. Clone the repository
2. Open in Android Studio
3. Add your Firebase configuration file (`google-services.json`) to the `app/` directory
4. Add your Firebase API key to `local.properties`:
   ```
   FIREBASE_API_KEY=your_api_key_here
   ```
5. Build and run the project

## Dependencies

- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Picasso (for image loading)
- Android Jetpack components (Navigation, ViewModel, etc.)

## Firebase Kurulumu (Turkish - Firebase Setup)

Firebase yapılandırması için aşağıdaki adımları izleyin:

1. Firebase (console.firebase.google.com) adresine gidin
2. Yeni bir proje oluşturun veya mevcut bir projeyi seçin
3. Android uygulaması ekleyin ve paket adı olarak `com.ender.artfolio` kullanın
4. Oluşturulan `google-services.json` dosyasını indirin
5. Bu dosyayı projenin `app/` dizinine yerleştirin
6. Uygulamayı derleyin ve çalıştırın

Firebase yapılandırması, hassas bilgilerin (API anahtarları, vb.) kaynak koduna doğrudan yazılmasını önlemek için yapılmıştır. Tüm gizli bilgiler `.gitignore` dosyası tarafından dışlanmıştır ve bu nedenle GitHub'a yüklenmez.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a pull request