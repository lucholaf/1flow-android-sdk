package com.oneflow.analytics.model.adduser

import com.google.gson.annotations.SerializedName

data class OFAddUserReq(@SerializedName ("user_id") val userId:String, @SerializedName("context")val ofAddUserContext: OFAddUserContext)
