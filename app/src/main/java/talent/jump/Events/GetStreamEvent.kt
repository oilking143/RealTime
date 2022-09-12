package talent.jump.Events

import talent.jump.data.LiveStreamListResponse

class GetStreamEvent internal constructor(streamResponse: LiveStreamListResponse){
    private var streamResponse: LiveStreamListResponse

    init {
        this.streamResponse = streamResponse
    }

    fun GetLiveStreamList(): LiveStreamListResponse
    {
        return streamResponse
    }
}

