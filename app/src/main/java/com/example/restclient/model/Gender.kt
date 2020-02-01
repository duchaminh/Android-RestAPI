package com.example.restclient.model

class Gender {
    var genderId:Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var genderName:String = ""
        get() = field        // getter
        set(value) {         // setter
            field = value
        }

    override fun toString(): String {
        return this.genderName
    }
}