package com.ordolabs.compose.di

import androidx.room.Room
import com.ordolabs.data.datastore.NoNameDao
import com.ordolabs.data.room.ComposeDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RoomModule = module {

    single {
        Room
            .databaseBuilder(androidContext(), ComposeDB::class.java, "chessmate_db")
            .build()
    }

    // DAOs
    single {
        val db: ComposeDB = get()
        provideNonameDao(db)
    }

    // Repositories
//    single<IRoomMovesRepository> {
//        val dao: MovesDao = get()
//        MovesRepository(dao)
//    }
}

internal fun provideNonameDao(db: ComposeDB): NoNameDao {
    return db.nonameDao
}