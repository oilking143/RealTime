package talent.jump.Events

class PhotoEvent internal constructor(photo: String)  {

    private var photo: String = photo
    init {
        this.photo = photo
    }
    internal fun getPhoto(): String {
        return photo
    }


}