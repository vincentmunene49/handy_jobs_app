package com.example.handyjobs.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.handyjobs.database.HandyJobsDao
import com.example.handyjobs.database.HandyJobsDatabase
import com.example.handyjobs.util.TABLE_NAME
import com.example.handyjobs.viewmodel.repository.HandyJobsDbRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()//get firebase auth

    @Provides
    @Singleton

    fun provideFireBaseFirestoreDb() = Firebase.firestore


    @Provides
    @Singleton

    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        HandyJobsDatabase::class.java,
        TABLE_NAME,
    ).fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton

    fun provideDao(db:HandyJobsDatabase) = db.HandyJobsDao()

    @Provides
    @Singleton
    fun provideRepository(dao: HandyJobsDao) = HandyJobsDbRepository(dao)


}