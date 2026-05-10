package com.example.tugaspam3.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugaspam3.data.*
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.platform.AndroidDeviceInfo
import com.example.tugaspam3.platform.AndroidNetworkMonitor
import com.example.tugaspam3.platform.DeviceInfo
import com.example.tugaspam3.platform.NetworkMonitor
import com.example.tugaspam3.viewmodel.NotesViewModel
import com.example.tugaspam3.viewmodel.ProfileViewModel
import com.example.tugaspam3.viewmodel.SettingsViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    // Database & SQLDelight - Renamed database to avoid downgrade error on local devices
    single<SqlDriver> {
        AndroidSqliteDriver(NoteDatabase.Schema, androidContext(), "notes_v2.db")
    }
    single { NoteDatabase(get()) }
    
    // Data Sources
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single { SettingsManager(androidContext()) }
    
    // AI Services
    single {
        val apiKey = "AIzaSyB2vdIx2z3Wgp3Oy52arpY93coCi2PkKbI"
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 40
                topP = 0.95f
                maxOutputTokens = 1024
            }
        )
    }
    single<GeminiService> { GeminiServiceImpl(get()) }
    
    // Platform Features
    single<DeviceInfo> { AndroidDeviceInfo(androidContext()) }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
}

val viewModelModule = module {
    viewModel { NotesViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel() }
}

val appModule = listOf(dataModule, viewModelModule)
