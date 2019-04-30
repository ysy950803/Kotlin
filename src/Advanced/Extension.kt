@file:JvmName("Ext")
package Advanced

import java.io.File

val topLevel = "top"
fun showTopLevel() = print(topLevel)

class Extension {

    companion object {
        val TAG = "Extension"
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
