package com.oneflow.analytics.model.adduser

import java.util.*

data class OFAddUserContext(
        val app: HashMap<String,String>,
        val device: HashMap<String,String>,
        val library: HashMap<String,String>,
        val network: HashMap<String,Object>,
        val screen: HashMap<String,String>,
        val os: HashMap<String,String>)
