package talent.jump.Events

import android.net.Uri

class UriEvent internal constructor(uri: Uri)  {

    private var uri: Uri = uri
    init {
        this.uri = uri
    }
    internal fun getUri(): Uri {
        return uri
    }


}