package com.example.happy.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.happy.model.*

@Database(entities = [
            BillItem::class,
            Cleaning::class,
            Meeting::class,
            Member::class,
            Notification::class,
            Shopping::class,
            Rep::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao() : MemberDAO
    abstract fun billDao() : BillDAO
    abstract fun cleaningDao() : CleaningDAO
    abstract fun meetingDao() : MeetingDAO
    abstract fun notificationDao() : NotificationDAO
    abstract fun shoppingDao() : ShoppingDAO
    abstract fun repDao() : RepDAO

    companion object {

        @Volatile
        private var INSTANSE: AppDatabase? = null

        fun getDatabase(context: Application) : AppDatabase {
            return INSTANSE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "happy_database.db")
                    //.fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                   // .createFromAsset("happy_database.db")
                    .build()
                    INSTANSE = instance

                instance
            }
        }
    }

}