package com.oneflow.analytics.model.adduser

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "User")
data class OFAddUserResponse(
        @PrimaryKey
        @ColumnInfo(name = "analytic_user_id")
        @SerializedName("analytic_user_id")
        var analytic_user_id: String,

        @ColumnInfo(name = "first_name")
        @SerializedName("first_name")
        val first_name: String?,

        @ColumnInfo(name = "last_name")
        @SerializedName("last_name")
        val last_name: String? = null,

        @ColumnInfo(name = "system_id")
        @SerializedName("system_id")
        var system_id: String? = null,

        @ColumnInfo(name = "created_on")
        @SerializedName("created_on")
        var created_on: Long,

        @ColumnInfo(name = "updated_on")
        @SerializedName("updated_on")
        var updated_on: Long
)
