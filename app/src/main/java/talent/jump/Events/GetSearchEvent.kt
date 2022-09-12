package talent.jump.Events

import talent.jump.data.SearchResponse

class GetSearchEvent internal constructor(searchResponse: SearchResponse){
    private var searchResponse: SearchResponse

    init {
        this.searchResponse = searchResponse
    }

    fun GetSearchList(): SearchResponse
    {
        return searchResponse
    }
}

