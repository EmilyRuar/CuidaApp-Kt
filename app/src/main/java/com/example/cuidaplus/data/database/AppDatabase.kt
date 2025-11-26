
package com.example.cuidaplus.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cuidaplus.data.dao.AppointmentDao
import com.example.cuidaplus.data.dao.PatientDao
import com.example.cuidaplus.data.dao.MedicalServiceDao
import com.example.cuidaplus.data.model.Appointment
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.data.model.MedicalService

@Database(
    entities = [Patient::class, MedicalService::class, Appointment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun medicalServiceDao(): MedicalServiceDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cuidaplus_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
