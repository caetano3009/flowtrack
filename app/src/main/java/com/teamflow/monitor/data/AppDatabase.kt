package com.teamflow.monitor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TeamMember::class, CheckIn::class, ProductionRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun checkInDao(): CheckInDao
    abstract fun productionRecordDao(): ProductionRecordDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flowtrack.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { seedDatabase(it) }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        // Seed data intentionally mixes different kinds of work (production, service,
        // field work) to show the app fits many types of business, not just one.
        private suspend fun seedDatabase(db: AppDatabase) {
            val memberDao = db.teamMemberDao()
            val recordDao = db.productionRecordDao()

            memberDao.insert(TeamMember(name = "Marina Souza", contact = "(11) 98765-0001", schedule = "Seg, Qua", active = true, checkinCount = 12))
            memberDao.insert(TeamMember(name = "Pedro Almeida", contact = "(11) 98765-0002", schedule = "Ter, Qui", active = true, checkinCount = 8))
            memberDao.insert(TeamMember(name = "Rafael Costa", contact = "(11) 98765-0003", schedule = "Sex", active = true, checkinCount = 15))
            memberDao.insert(TeamMember(name = "Juliana Nunes", contact = "(11) 98765-0004", schedule = "Sáb", active = false, checkinCount = 3))

            recordDao.insert(ProductionRecord(item = "Peças montadas", quantity = 120.0, unit = "unidades", date = "2025-05-05", memberName = "Marina S."))
            recordDao.insert(ProductionRecord(item = "Atendimentos realizados", quantity = 34.0, unit = "unidades", date = "2025-05-04", memberName = "Pedro A."))
            recordDao.insert(ProductionRecord(item = "Horas em campo", quantity = 22.0, unit = "horas", date = "2025-05-03", memberName = "Rafael C."))
            recordDao.insert(ProductionRecord(item = "Produtos embalados", quantity = 340.0, unit = "unidades", date = "2025-05-02", memberName = "Marina S."))
            recordDao.insert(ProductionRecord(item = "Chamados resolvidos", quantity = 15.0, unit = "unidades", date = "2025-05-01", memberName = "Juliana N."))
            recordDao.insert(ProductionRecord(item = "Metros instalados", quantity = 60.0, unit = "m", date = "2025-04-30", memberName = "Pedro A."))
        }
    }
}
