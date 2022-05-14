package talent.jump.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import talent.jump.data.*

interface ApiRequest {

    @POST("/Login")
    fun Login(@Body data: JsonObject): Call<LoginResponse>

    @POST("/MembershipRegister")
    fun MembershipRegister(@Body data: JsonObject): Call<RegisterResponse>

    @GET("/api/user")
    fun getMe(): Call<GetUserResponse>

    @PUT("/api/user")
    fun updateMe(@Body data: JsonObject): Call<UpdateUserResponse>

    @PUT("/api/user/password/reset")
    fun resetPassword(@Body data: JsonObject): Call<UpdateUserResponse>

    @PUT("/api/user/password")
    fun updatePassword(@Body data: JsonObject): Call<UpdateUserResponse>

    @POST("/api/user/verify/email/send")
    fun verifiedEmailMessage(@Body data: JsonObject): Call<UpdateUserResponse>

    @POST("/api/user/verify/email")
    fun verifyMail(@Body data: JsonObject): Call<RegisterResponse>



}