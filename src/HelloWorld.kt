/**
 * 看官方文档时的一些学习笔记，很零散，权当记录
 * 总的感觉来说，这门语言很次时代
 * 虽然基于JVM，也兼容Java，但和前者有很大不同
 * Created by Sylvester on 2017/6/6.
 */
fun main(args: Array<String>) {
//    println("Hello, world!")
//    doFun()
//    printHello(null)
//    val array = arrayOf(1, 2, 3)
//    val arrList = arrayListOf(3, 2, 1)
//    println(array)
//    println(arrList)
//    println(asList(1, 2, *array, 3))
//    val pMin = Person(110)
//    pMin.printName("I am pMin.")
//    checkNumber()
    tryOperator()
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

fun doFun() {
    val l = mutableListOf(1, 2, 3)
    l.swap(1, 2)
    print(l)
    print(box.value)
}

class Box<T>(t: T) {
    var value = t
}

val box = Box(2)

abstract class Source<out T> {
    abstract fun nextT(): T
}

fun doTOut(strs: Source<String>) {
    val objs: Source<Any> = strs
}

abstract class Comparable<in T> {
    abstract fun compareTo(other: T): Int
}

fun doTIn(x: Comparable<Number>) {
    x.compareTo(1.2)
    val y: Comparable<Double> = x
}

fun printHello(name: String?): Unit {
    println("Hello! " + name.toString())
}

fun <T> asList(vararg items: T): List<T> {
    val list = ArrayList<T>()
    list += items
//    for (item in items)
//        list.add(item)
    return list
}

class Person(val num: Int) {

    init {
        printName("name")
        println("Person Init.")
    }

    fun printName(name: String?) {
        println(name + " num:" + num)
    }
}

fun checkNumber() {
    val a: Int = 100
    println(a == a)
    println(a === a)
    val a1: Int? = a
    val a2: Int? = a
    println(a1 == a2)
    println(a1 === a2)

    val b: Long = a.toLong()
    val c: Char = b.toChar()
    val s: String = b.toString()
    println("" + b + "\n" + c + "\n" + s)

    val ab: Long = a + b
    val ba = b + a // 虽然Kotlin不能隐式转换基本数据类型，但可以通过上下文推断出类型，这也算是一种变相的强制转换，体现了协变性
}

data class Point<T>(var x: T, var y: T)
data class Point1<out T>(val x: T, val y: T)

// 操作符符重载
operator fun Point<Int>.unaryPlus() = Point(x, y)
operator fun Point<Int>.unaryMinus() = Point(-x, -y)
operator fun Point<Boolean>.not() = Point(!x, !y)

data class Line<T>(var len: T)

operator fun Line<Int>.inc() = Line(len++)

fun tryOperator() {
    val p = Point(10, 20)
    println(-p)
    println(p.unaryMinus())

    val line = Line(1)
    println(line.inc()) // 后自增，此行打印为1
    println(line) // 2
}
