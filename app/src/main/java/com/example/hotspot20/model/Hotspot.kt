package com.example.hotspot20.model

import java.io.Serializable

class Hotspot (var name: String, var latitude: Double, var longitude: Double): Serializable {

    constructor():this("", 0.0, 0.0)

}