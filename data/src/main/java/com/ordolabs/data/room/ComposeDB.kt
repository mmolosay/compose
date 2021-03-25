package com.ordolabs.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ordolabs.data.datastore.NoNameDao
import com.ordolabs.data.room.entity.NoNameEntity

@Database(entities = [NoNameEntity::class], version = 1, exportSchema = false)
abstract class ComposeDB : RoomDatabase() {

    abstract val nonameDao: NoNameDao
}