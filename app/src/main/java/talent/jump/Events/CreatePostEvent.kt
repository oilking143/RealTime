package talent.jump.Events

import talent.jump.data.CreatePostResponse

class CreatePostEvent internal constructor(createpost: CreatePostResponse){
    private var createpost: CreatePostResponse

    init {
        this.createpost = createpost
    }

    fun GetPost(): CreatePostResponse
    {
        return createpost
    }
}

