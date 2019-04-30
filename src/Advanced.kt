import java.io.File

class Advanced {

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
        
        f.bufferedReader().use { 
            print(it.readText())
        }
    }
}
