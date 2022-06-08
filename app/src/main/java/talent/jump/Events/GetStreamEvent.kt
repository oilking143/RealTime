package talent.jump.Events

import talent.jump.data.StreamListResponse

class GetStreamEvent internal constructor(streamResponse: StreamListResponse){
    private var streamResponse: StreamListResponse

    init {
        this.streamResponse = streamResponse
    }

    fun GetStreamResponse(): StreamListResponse
    {
        return streamResponse
    }
}

