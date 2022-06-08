package talent.jump.Events

import talent.jump.data.CreateStreamResponse

class CreateStreamEvent internal constructor(streamResponse: CreateStreamResponse){
    private var createstreamResponse: CreateStreamResponse

    init {
        this.createstreamResponse = streamResponse
    }

    fun GetStreamResponse(): CreateStreamResponse
    {
        return createstreamResponse
    }
}

