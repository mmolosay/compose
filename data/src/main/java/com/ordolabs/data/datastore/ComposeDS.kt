package com.ordolabs.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore


class ComposeDS(context: Context) {

    private val Context.datastore by preferencesDataStore(
        name = DATASTORE_NAME
    )

//    val a = context.datastore.data

    companion object {
        private const val DATASTORE_NAME = "compose_data_store"
    }
}