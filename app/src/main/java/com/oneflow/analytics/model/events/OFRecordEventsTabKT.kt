package com.oneflow.analytics.model.events

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.oneflow.analytics.sdkdb.convertes.OFMapConverter
import java.util.*

@Entity(tableName = "RecordEvents")
class OFRecordEventsTabKT {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    var id = 0

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var eventName: String? = null

    @TypeConverters(OFMapConverter::class)
    @ColumnInfo(name = "parameters")
    @SerializedName("parameters")
    var dataMap: HashMap<String?, Any?>? = null

    @ColumnInfo(name = "time")
    @SerializedName("time")
    var time: Long? = null

    @ColumnInfo(name = "value")
    @SerializedName("value")
    var value: String? = null

    @ColumnInfo(name = "synced")
    @SerializedName("synced")
    var synced = 0

    @ColumnInfo(name = "created_on")
    @SerializedName("created_on")
    var createdOn: Long? = null


}