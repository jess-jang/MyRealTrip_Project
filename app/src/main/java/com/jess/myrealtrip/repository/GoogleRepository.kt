package com.jess.myrealtrip.repository

import com.google.gson.Gson
import com.jess.myrealtrip.common.util.tryCatch
import com.jess.myrealtrip.data.ChannelData
import com.jess.myrealtrip.data.RssData
import com.jess.myrealtrip.repository.service.GoogleService
import fr.arnaudguyon.xmltojsonlib.XmlToJson

/**
 * General Repository
 */
interface GoogleRepository {
    suspend fun getList(): ChannelData?
}

class GoogleRepositoryImpl constructor(
    private val service: GoogleService
) : GoogleRepository {

    /**
     * XML을 Data Class로 변환
     * com.github.smart-fun:XmlToJson 사용
     * @return
     */
    override suspend fun getList(): ChannelData? {
        tryCatch {
            val response = service.getList().body()
            response?.let {
                val json = XmlToJson.Builder(response).build().toJson().toString()
                val data = Gson().fromJson(json, RssData::class.java)
                return data.rss?.channel
            }
        }
        return null
    }
}
