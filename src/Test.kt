import java.util.*
import kotlin.math.sin
import kotlin.math.sqrt

fun main() {
//    makeLoveHeart()
    println(countPrimes(100))

    printMixMap("1" to "2", "2" to "3")
}

fun printMixMap(vararg pairs: Pair<String, Any?>) {
    mapOf(*pairs, "test" to "1").forEach {
        println("${it.key} to ${it.value}")
    }
}

fun makeLoveHeart() {
    for (i in 1..520) {
        println("${getSpace(i)}$i❤")
    }
}

fun getSpace(x: Int): String {
    var space = " "
    val y = (sin(Math.PI / 16 * x) * 20 + 20).toInt()
    for (i in 0..y) {
        space += " "
    }
    return space
}

fun countPrimes(n: Int): Int {
    val isPrim = BooleanArray(n)
    Arrays.fill(isPrim, true)

    for (i in 2 until sqrt(n.toDouble()).toInt()) {
        if (isPrim[i]) {
            val i2 = i * i
            for (j in i2 until n step i) {
                isPrim[j] = false
            }
        }
    }

//    var i = 2
//    while (i * i < n) {
//        if (isPrim[i]) {
//            var j = i * i
//            while (j < n) {
//                isPrim[j] = false
//                j += i
//            }
//        }
//        i++
//    }

    var count = 0
    for (k in 2 until n) if (isPrim[k]) count++
    return count
}
