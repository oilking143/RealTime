package talent.jump.Fragment

import android.util.Log
import androidx.fragment.app.Fragment
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

abstract class BaseFragment: Fragment()  {


    fun md5(input: String): String {

        val md5String=input+""
        Log.d("md5String", md5String)
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(md5String.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getRandom(min: Int, max: Int): Float {
        require(min < max) { "Invalid range [$min, $max]" }
        return (min + Random.nextFloat() * (max - min))
    }


}