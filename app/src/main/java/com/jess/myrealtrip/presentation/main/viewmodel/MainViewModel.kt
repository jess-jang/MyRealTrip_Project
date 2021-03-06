package com.jess.myrealtrip.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jess.myrealtrip.common.base.BaseViewModel
import com.jess.myrealtrip.common.extension.safeScope
import com.jess.myrealtrip.data.NewsData
import com.jess.myrealtrip.data.RssResponseData.RssData.ChannelData.ItemData
import com.jess.myrealtrip.repository.GoogleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author jess
 * @since 2020.03.20
 */
class MainViewModel @Inject constructor(
    private val repository: GoogleRepository
) : BaseViewModel() {

    private val _item = MutableLiveData<List<NewsData>>()
    val item: LiveData<List<NewsData>> = _item

    /**
     * News 정보 가져오기
     */
    fun getNews() {

        _isProgress.value = true

        CoroutineScope(ioDispatchers).safeScope().launch {
            repository.getList()?.let { channel ->
                CoroutineScope(uiDispatchers).safeScope().launch {
                    val list = mutableListOf<NewsData>()
                    channel.item?.forEach { item ->
                        list.add(NewsData(item.title, item.link, item.source))
                    }
                    _item.postValue(list)
                    _isProgress.postValue(false)
                }
            }
        }
    }
}