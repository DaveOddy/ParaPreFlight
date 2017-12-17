package com.davidoddy.parapreflight

import android.app.Application
import android.arch.persistence.room.Room
import com.davidoddy.parapreflight.dao.AppDatabase
import timber.log.Timber

/**
 * Created by DOddy on 12/13/17.
 */
class ParaPreFlightApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        ParaPreFlightApplication.database = Room.databaseBuilder(this, AppDatabase::class.java, "para-checklist-db").build()
        Thread({
            ParaPreFlightApplication.database?.itemDAO()?.deleteAllItems()
        }).start()

    }

}