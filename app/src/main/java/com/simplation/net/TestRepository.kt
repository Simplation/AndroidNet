package com.simplation.net

import com.simplation.codelab.bean.History
import com.simplation.codelab.bean.Journalism
import com.simplation.net.config.RequestMethod

class TestRepository : BaseRepository() {

    suspend fun fetchHistoryList(): ArrayList<History> {
        return request(
            requestMethod = RequestMethod.POST,
            function = "lishi/api.php"
        )
    }

    fun fetchJournalismList(): ArrayList<Journalism> {
        return requestSync(
            requestMethod = RequestMethod.GET,
            baseUrl = "https://is.snssdk.com/",
            function = "api/news/feed/v51/",
            parserCls = CustomParser2::class
        )
    }
}