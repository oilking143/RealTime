package talent.jump.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import talent.jump.data.LoginResponse
import talent.jump.data.RegisterResponse

interface ApiRequest {

    @POST("/Login")
    fun Login(@Body data: JsonObject): Call<LoginResponse>

    @POST("/MembershipRegister")
    fun MembershipRegister(@Body data: JsonObject): Call<RegisterResponse>
}