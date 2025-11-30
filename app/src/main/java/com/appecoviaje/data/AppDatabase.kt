package com.appecoviaje.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appecoviaje.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Trip::class, Experience::class, Reservation::class, User::class], version = 17, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun experienceDao(): ExperienceDao
    abstract fun reservationDao(): ReservationDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                
                Instance = instance

                // Robust seeding: Check if empty and seed
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (instance.tripDao().count() == 0) {
                            seed(instance.tripDao())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                instance
            }
        }

        suspend fun seed(tripDao: TripDao) {
            val initialTrips = listOf(
                Trip(title = "Torres del Paine", description = "El circuito de trekking más famoso de Chile.", location = "Parque Nacional Torres del Paine", latitude = -51.0, longitude = -73.0, imageResId = R.drawable.paisaje_torres_del_paine, category = "Montaña"),
                Trip(title = "San Pedro de Atacama", description = "Un oasis en medio del desierto más árido.", location = "San Pedro de Atacama", latitude = -22.9, longitude = -68.2, imageResId = R.drawable.paisaje_desierto_de_atacama, category = "Desierto"),
                Trip(title = "Isla de Pascua", description = "La isla más remota, famosa por sus moáis.", location = "Isla de Pascua", latitude = -27.1, longitude = -109.4, imageResId = R.drawable.rapanui, category = "Isla"),
                Trip(title = "Valparaíso", description = "Cerros coloridos y arte callejero.", location = "Región de Valparaíso", latitude = -33.0, longitude = -71.6, imageResId = R.drawable.valpo, category = "Ciudad"),
                Trip(title = "Valle del Elqui", description = "Cuna del pisco y cielos estrellados.", location = "Valle del Elqui", latitude = -30.0, longitude = -70.5, imageResId = R.drawable.paisaje_valle_de_la_luna, category = "Valle")
            )
            initialTrips.forEach { tripDao.insert(it) }
        }
    }
}
