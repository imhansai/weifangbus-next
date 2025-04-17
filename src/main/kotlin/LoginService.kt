import com.fasterxml.jackson.databind.node.ObjectNode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(Constants.Api.MEMBER_GET_OTP)
    fun getOtp(@Body jSONObject: ObjectNode): Call<ObjectNode>

    @POST(Constants.Api.MEMBER_LOGIN)
    fun login(@Body jSONObject: ObjectNode): Call<ObjectNode>

}