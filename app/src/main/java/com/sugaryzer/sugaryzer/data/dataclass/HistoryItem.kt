package com.sugaryzer.sugaryzer.data.dataclass

import com.sugaryzer.sugaryzer.data.response.DataItemHistory

sealed class HistoryItem {
    data class Header(val date: String) : HistoryItem()
    data class Content(val dataItem: DataItemHistory) : HistoryItem()
}
