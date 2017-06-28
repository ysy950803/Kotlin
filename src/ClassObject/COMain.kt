package ClassObject

/**
 * 类和对象
 * Created by Sylvester on 2017/6/16.
 */
fun main(args: Array<String>) {
    val cust = Customer("C")
    val innerDemo = Outer.Nested().foo()
    println(innerDemo)
}

class Invoice {
}

// 像上述这种没有类体的时候可以直接如下定义
class Empty

class Person constructor(name: String) {
}

class Person2(name: String = "P2") {
}

class Customer(name: String) {
    init {
        println(name)
    }
}

// 主构造的参数可以在初始化块中使用，也可以在类体内声明的属性初始化器中使用
class Customer2(name: String) {
    val key = name.toUpperCase()

    init {
        println(key)
    }
}

class Person3(val firstName: String = "P3", val lastName: String) {
}

// 如果类有一个主构造函数，每个次构造函数需要委托给主构造函数
// 可以直接委托或者通过 别的次构造函数间接委托
// 委托到同一个类的另一个构造函数用this关键字即可
class Person4(val name: String = "P4") {
    constructor(name: String, parent: Person4) : this(name) {
        println(name)
    }
}

// 如果一个非抽象类没有声明任何(主或次)构造函数，它会有一个生成的不带参数的主构造函数
// 构造函数的可见性是public。如果你不希望你的类有一个公有构造函数，你需要声明一个带有非默认可见性的空的主构造函数
class DontCreateMe private constructor() {
}

// 嵌套类
class Outer {
    private val bar: Int = 1

    class Nested {
        fun foo() = Outer2().Inner().foo()
    }
}

// 内部类
class Outer2 {
    private val bar: Int = 2

    inner class Inner {
        fun foo() = bar
    }
}
