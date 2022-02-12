package com.batyrzhan.autoparts.utils

class Constant {
    companion object {
        const val BASE_URL: String = "https://autopartsmyshop.000webhostapp.com"
        const val POST_ORDER: String = "$BASE_URL/api/api.php?post_order"
        //set false if you want price to be displayed in decimal
        const val ENABLE_DECIMAL_ROUNDING = true
        //set true if you want to enable RTL (Right To Left) mode, e.g : Arabic Language
        const val ENABLE_RTL_MODE = false
    }
}