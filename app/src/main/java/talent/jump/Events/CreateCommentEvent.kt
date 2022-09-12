package talent.jump.Events

import talent.jump.data.CommentResponse

class CreateCommentEvent internal constructor(cerateResponse: CommentResponse){
    private var cerateResponse: CommentResponse

    init {
        this.cerateResponse = cerateResponse
    }

    fun GetCreatedata(): CommentResponse
    {
        return cerateResponse
    }
}

