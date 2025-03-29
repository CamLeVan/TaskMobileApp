package com.example.taskapplication.data.local.database

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskapplication.data.local.dao.ChatMessageDao
import com.example.taskapplication.data.local.dao.PersonalTaskDao
import com.example.taskapplication.data.local.dao.TeamDao
import com.example.taskapplication.data.local.dao.TeamMemberDao
import com.example.taskapplication.data.local.dao.TeamTaskAssignmentDao
import com.example.taskapplication.data.local.dao.TeamTaskDao
import com.example.taskapplication.data.local.entities.ChatMessageEntity
import com.example.taskapplication.data.local.entities.PersonalTaskEntity
import com.example.taskapplication.data.local.entities.TeamEntity
import com.example.taskapplication.data.local.entities.TeamMemberEntity
import com.example.taskapplication.data.local.entities.TeamTaskAssignmentEntity
import com.example.taskapplication.data.local.entities.TeamTaskEntity

@Database(
    entities = [
        PersonalTaskEntity::class,
        TeamEntity::class,
        TeamMemberEntity::class,
        TeamTaskEntity::class,
        TeamTaskAssignmentEntity::class,
        ChatMessageEntity::class
    ],
    version = 1
)
abstract class AppDatabase() : RoomDatabase(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    abstract fun personalTaskDao(): PersonalTaskDao
    abstract fun teamDao(): TeamDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun teamTaskDao(): TeamTaskDao
    abstract fun teamTaskAssignmentDao(): TeamTaskAssignmentDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_manager_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppDatabase> {
        override fun createFromParcel(parcel: Parcel): AppDatabase {
            return AppDatabase(parcel)
        }

        override fun newArray(size: Int): Array<AppDatabase?> {
            return arrayOfNulls(size)
        }
    }
}