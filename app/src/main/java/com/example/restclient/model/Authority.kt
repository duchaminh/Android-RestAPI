package com.example.restclient.model


class Authority() {

     var authorityId: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
     var authorityName: String = ""
        get() = field        // getter
        set(value) {         // setter
            field = value
        }

    override fun toString(): String {
        return this.authorityName
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }


}