package com.example.emergen_app.di

import com.example.emergen_app.data.repository.AccountRepositoryImpl
import com.example.emergen_app.data.repository.StorageFirebaseRepositoryImpl
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModuleImpl {

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun provideStorageService(impl: StorageFirebaseRepositoryImpl): StorageFirebaseRepository


}