package com.example.mypracticeapplication.coding_concepts

import android.util.Log


data class User(val name: String, val email: String)

fun runRun(){

    val user: User? = null //User("foo", "bar")


    val emailLength = user?.run { email.length }

    Log.i("run","This is the email lenght $emailLength")

}