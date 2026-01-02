package com.example.stopwatchapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao() : RecordDao

    companion object {
        @Volatile //マルチスレッドでCPUのキャッシュメモリを使用せず、メインメモリで一意のインスタンスを保障
        private var DB: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase {
            return DB ?: synchronized(this) { // synchronizedで他のスレッドをロックし１つのスレッドでのみインスタンス生成
                return DB ?: Room.databaseBuilder( // 仮にロック待ちのスレッドが発生してもブロック内で再度DBインスタンスの非nullを確認
                    context,
                    RecordDatabase::class.java,
                    "record_database"
                ).build().also{ DB = it }
            }
        }
    }
}