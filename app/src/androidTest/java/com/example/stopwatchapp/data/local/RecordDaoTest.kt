package com.example.stopwatchapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class RecordDaoTest {
    private lateinit var recordDao: RecordDao
    private lateinit var db: RecordDatabase

    private fun generateRandomDate(): String {
        val year = 2024 + Random.nextInt(3)
        val month = Random.nextInt(1, 13)
        val day = Random.nextInt(1, 28)
        return "%04d/%02d/%02d".format(year, month, day)
    }

    private fun generateRandomWord(): String {
        val wordList = listOf<String>("買い物", "運動", "料理", "仕事", "散髪")
        return wordList[Random.nextInt(5)]
    }
    private fun createSampleRecord(
        id: Long = 1, title: String = "タイトル", description: String = "内容", recordDate: String = "2026/01/01")
    = Record(
        id = id,
        title = title,
        description = description,
        time = "12:34.56",
        recordDate = recordDate
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RecordDatabase::class.java).build()
        recordDao = db.recordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // 1件のレコードを作成し、その作成した1件が読み込まれるか確認するテスト
    @Test
    @Throws(Exception::class)
    fun writeRecordAndReadRecord() = runTest{
        // --- 作成 ---
        val sampleRecord = createSampleRecord()
        recordDao.insert(sampleRecord)

        // --- 取得 ---
        val fetchedRecordById: Record? = recordDao.getRecordById(1).first()

        // --- テスト ---
        assertThat(fetchedRecordById, equalTo(sampleRecord))
    }

    // 1件のレコードを作成し、その作成したレコードのタイトルと内容を更新後、取得したレコードが更新後の内容か確認するテスト
    @Test
    @Throws(Exception::class)
    fun writeRecordAndUpdateRecordAndReadRecord() = runTest{
        // --- 作成 ---
        val sampleRecord = createSampleRecord()
        recordDao.insert(sampleRecord)

        // --- 更新 ---
        val updateRecord = createSampleRecord( title = "更新後タイトル",description = "更新後内容")

        recordDao.update(updateRecord)

        // --- 取得 ---
        val fetchedRecord: Record? = recordDao.getRecordById(1).first()

        // --- テスト ---
        assertThat(fetchedRecord, equalTo(updateRecord))
    }

    // 1件のレコードを作成し、その作成したレコードを削除し、存在しないことを確認するテスト
    @Test
    @Throws(Exception::class)
    fun writeRecordAndDeleteRecord() = runTest{
        // --- 作成 ---
        val sampleRecord = createSampleRecord()
        recordDao.insert(sampleRecord)

        // --- 削除 ---
        recordDao.delete(sampleRecord)

        // --- 取得 ---
        val fetchedRecord: Record? = recordDao.getRecordById(1).firstOrNull()

        // --- テスト ---
        assertNull(fetchedRecord)
    }

    // 5件のレコードを作成し、その作成したレコードのリストを取得しすべて存在し且つ日付順(DESC)になっているか確認するテスト
    @Test
    @Throws(Exception::class)
    fun writeRecordsAndReadRecordsOrderedByRecordDateDesc() = runTest{
        // --- 作成 ---
        val sampleRecordList = mutableListOf<Record>()
        for (i in 1..5) {
            sampleRecordList.add( createSampleRecord( id = i.toLong(), recordDate = generateRandomDate() ) )
            recordDao.insert(sampleRecordList[i-1])
        }

        // --- 取得 ---
        val fetchedRecordList: List<Record> = recordDao.getRecords().first()

        // --- テスト ---
        val expectedRecordListOrderedByRecordDate = sampleRecordList.sortedByDescending { it.recordDate}
        for (i in 0..4) {
            assertThat( fetchedRecordList[i], equalTo(expectedRecordListOrderedByRecordDate[i]) )
        }
    }

    // 10件のレコードを作成し、その作成したレコードの中から検索文字列にヒットしたもののみ返ってくるか確認するテスト
    @Test
    @Throws(Exception::class)
    fun writeRecordsAndReadRecordsWithSearchWordOrderedByRecordDateDesc() = runTest{
        // --- 作成 ---
        val sampleRecordList = mutableListOf<Record>()
        for (i in 1..10) {
            sampleRecordList.add( createSampleRecord(
                id = i.toLong(),
                title = generateRandomWord(),
                description = generateRandomWord(),
                recordDate = generateRandomDate() )
            )
            recordDao.insert(sampleRecordList[i-1])
        }



        // --- 取得 ---
        val searchWord = "運動"
        val fetchedRecordList: List<Record> = recordDao.getRecordsBySearchWord(searchWord).first()

        // --- テスト ---
        val orderedByRecordDate = sampleRecordList.sortedByDescending { it.recordDate}
        val expectedRecordListWithSearchWordOrderedByRecordDate = orderedByRecordDate.filter { record ->
            record.title == searchWord || record.description == searchWord
        }
        val until = expectedRecordListWithSearchWordOrderedByRecordDate.size - 1
        for (i in 0..until) {
            assertThat( fetchedRecordList[i], equalTo(expectedRecordListWithSearchWordOrderedByRecordDate[i]) )
        }
    }



}