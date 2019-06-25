package com.vipin.mygatekotlin.model

/**
 * Created by vipin.c on 25/06/2019
 */
class DataClass(){


    var id: Int = 0

    var randomNum: Int = 0
        internal set
    var image: ByteArray ?= null
        internal set


    constructor(id: Int, randomNum: Int, image: ByteArray) : this() {
        this.id = id
        this.randomNum = randomNum
        this.image = image
    }

}