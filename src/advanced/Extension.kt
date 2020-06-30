@file:JvmName("ExtJVMName")

package advanced

import java.io.File
import java.util.*

const val TOP_LEVEL = "topLevel"
fun showTopLevel() = println(TOP_LEVEL)

class Extension {

//    companion object CREATOR : Parcelable.Creator<Extension>{
//    }

    object Singleton {

        fun doSomething() {
            println("doSomething")
        }

        @JvmStatic
        fun makeSomething() {
            println("makeSomething")
        }
    }

    companion object {
        const val TAG = "Extension"

        @JvmStatic
        fun staticFunc() {
        }

        val STATIC_OBJ = Date()
    }

    fun test1() {
        val string = String()

        string.apply {
            print("this:String")
        }

        string.also {
            print("it:String")
        }

        string.let {
            print("it:String")
        }

        string.run {
            print("this:String")
        }
    }

    fun test2() {
        val f = File("pathename")

        f.readLines().forEach {
            print(it)
        }

        val bytes = f.inputStream().use {
            return@use it.readBytes()
        }

        f.bufferedReader().use {
            print(it.readText())
        }
    }
}
