object Manager {

    private var _obj: String? = null

    val obj: String?
        get() = _obj?.takeIf { it.length == 2 } ?: StringBuilder().append("n").append("b").toString().apply {
            _obj = this
        }

    fun test() {
        _obj = "12"
        println(obj)
        _obj = null
        println(obj)
    }
}
