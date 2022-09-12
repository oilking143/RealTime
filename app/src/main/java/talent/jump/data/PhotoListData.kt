package talent.jump.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoListData(var photoList: List<Uri>): Parcelable
{

    fun getList():List<Uri> {
        return photoList
    }

}
