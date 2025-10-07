# 🎨 Artfolio - Sanat Paylaşım Uygulaması
Artfolio, sanatseverlerin eserlerini paylaşabileceği ve keşfedebileceği bir Android uygulamasıdır.
<img width="6735" height="2913" alt="Final" src="https://github.com/user-attachments/assets/bc1175b1-6a5d-446f-8660-5bcb47bcef5e" />

## 🚀 Başlangıç
- Android Studio
- Android SDK
- Firebase Entegrasyonu

## 🖼️ Özellikler
- Kullanıcı kaydı ve girişi
- Sanat eserlerini yükleme
- Sanat galerisi taraması
- Sanat eseri detaylarını görüntüleme

## 🧩 Mimari
Bu proje, MVVM (Model-View-ViewModel) mimarisini takip eder ve aşağıdaki bileşenleri içerir:

- Model: Veri sınıfları ve iş mantığı.
- View: Kullanıcı arayüzü bileşenleri.
- ViewModel: Veri yönetimi ve UI ile etkileşim.

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

## 🛠️ Teknolojiler
- Kotlin
- XML Components (Navigation, ViewModel)
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Picasso (Resim yükleme ve gösterme)
  
## 🔥 Firebase Kurulumu

Firebase yapılandırması için aşağıdaki adımları izleyin:

1. Firebase (console.firebase.google.com) adresine gidin
2. Yeni bir proje oluşturun veya mevcut bir projeyi seçin
3. Android uygulaması ekleyin ve paket adı olarak `com.ender.artfolio` kullanın
4. Oluşturulan `google-services.json` dosyasını indirin
5. Bu dosyayı projenin `app/` dizinine yerleştirin
6. Uygulamayı derleyin ve çalıştırın

Firebase yapılandırması, hassas bilgilerin (API anahtarları, vb.) kaynak koduna doğrudan yazılmasını önlemek için yapılmıştır. Tüm gizli bilgiler `.gitignore` dosyası tarafından dışlanmıştır ve bu nedenle GitHub'a yüklenmez.

## 📄 Lisans
Bu proje MIT Lisansı ile lisanslanmıştır. Daha fazla bilgi için LICENSE dosyasına bakınız.
