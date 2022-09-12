package talent.jump.Events

import talent.jump.data.PostmediaResponse

class PostMediaEvent internal constructor(postmediaResponse: PostmediaResponse){
    private var postmediaResponse: PostmediaResponse

    init {
        this.postmediaResponse = postmediaResponse
    }

    fun GetMedia(): PostmediaResponse
    {
        return postmediaResponse
    }
}

