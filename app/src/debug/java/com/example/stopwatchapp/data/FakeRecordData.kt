package com.example.stopwatchapp.data

import com.example.stopwatchapp.data.local.Record

object FakeRecordData {
    val testRecordList: List<Record> = List(10) { i ->
        Record(
            id = (i+1).toLong(),
            time = "12:34.56",
            recordDate = "2026/01/01 12:34:56 木曜日",
            title = "${i+1}kmランニング",
            description = "近所の公園を${i+1}周"
        )
    }
}