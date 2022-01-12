package com.alizaidi.regioninfo.db

import android.content.Context
import androidx.room.*
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.db.models.CountryRoom
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CountryRoom::class], version = 3)
@TypeConverters(Converters::class)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun CountryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: CountryDatabase? = null

        fun getDatabase(context: Context): CountryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryDatabase::class.java,
                    "my_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
