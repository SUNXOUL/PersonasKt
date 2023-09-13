package com.sagrd.personas.di

import android.content.Context
import androidx.room.Room
import com.sagrd.personas.data.PersonDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn ( SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTicketDatabase(@ApplicationContext appContext: Context): PersonDB =
        Room.databaseBuilder(
            appContext,
            PersonDB::class.java,
            "Ticket.db")
            .fallbackToDestructiveMigration()
            .build()
}