# Tugas 9 Pengembangan Aplikasi Mobile RB 

**Nama:** Raditya Alrasyid Nugroho  
**NIM:** 123140125  
**Kelas:** Pengembangan Aplikasi Mobile RB

# AI Integration App
## Deskripsi
Project Android dengan integrasi AI menggunakan Gemini API/OpenAI API untuk menambahkan fitur chatbot assistant pada aplikasi.

## Fitur
- AI Chat Assistant
- Multi-turn conversation
- Loading state
- Error handling
- Responsive UI
- Structured prompt engineering

---

# Teknologi

- Kotlin
- Jetpack Compose
- MVVM Architecture
- Retrofit
- Coroutines
- Gemini API / OpenAI API

---

# Implementasi AI

Aplikasi menggunakan API AI melalui service layer dengan alur:

```text
UI → ViewModel → Repository → API Service
```

Contoh system prompt:

```text
You are a helpful AI assistant inside a mobile application.
Provide concise and relevant answers for users.
```

---

# Error Handling

Aplikasi menangani:
- No internet connection
- API timeout
- Empty response
- Invalid request

Implementasi:
- Try-catch
- Error state UI
- Retry mechanism

---

# UI/UX

Fitur UI:
- Loading indicator
- Responsive chat screen
- Error message feedback
- Auto scroll chat

---

# Struktur Folder

```bash
app/
├── data/
├── network/
├── ui/
├── viewmodel/
└── repository/
```

---

# Menjalankan Project

## Clone Repository

```bash
git clone https://github.com/username/project-name.git
```

## Tambahkan API Key

Pada file `local.properties`

```properties
GEMINI_API_KEY=your_api_key
```

atau

```properties
OPENAI_API_KEY=your_api_key
```

---

# Run App

```bash
./gradlew installDebug
```

---

# Penilaian Tugas

| Kriteria | Status |
|----------|---------|
| AI Integration | ✅ |
| Prompt Engineering | ✅ |
| Error Handling | ✅ |
| UI/UX | ✅ |
| Clean Architecture | ✅ |

---
