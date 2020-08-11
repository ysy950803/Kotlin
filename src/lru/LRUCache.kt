package lru

class LRUCache(var capacity: Int) {

    private val cacheMap = LinkedHashMap<Int, Int>()

    fun get(key: Int): Int {
        if (!cacheMap.contains(key)) {
            return -1
        }
        return moveToRecent(key, cacheMap[key]!!)
    }

    fun put(key: Int, value: Int) {
        if (cacheMap.contains(key)) {
            moveToRecent(key, value)
            return
        }

        if (cacheMap.size >= capacity) {
            // 第一个是最旧的数据
            cacheMap.remove(cacheMap.keys.iterator().next())
        }
        cacheMap[key] = value
    }

    private fun moveToRecent(key: Int, value: Int): Int {
        cacheMap.remove(key)
        cacheMap[key] = value
        return value
    }
}

class Node(var key: Int, var value: Int) {
    var prev: Node? = null
    var next: Node? = null
}

class LinkedNodeList {

    private val head = Node(-1, -1)
    private val tail = Node(-1, -1)

    init {
        head.next = tail
        tail.prev = head
    }

    fun addRecent(node: Node): Node {
        node.prev = tail.prev
        node.next = tail
        tail.prev?.next = node
        tail.prev = node
        return node
    }

    fun moveToRecent(node: Node): Int {
        remove(node)
        addRecent(node)
        return node.value
    }

    fun removeOldest(): Int {
        val oldest = head.next
        remove(oldest!!)
        return oldest.key
    }

    private fun remove(node: Node) {
        node.prev?.next = node.next
        node.next?.prev = node.prev
        node.prev = null
        node.next = null
    }
}

class LRUCache2(var capacity: Int) {

    private val map = HashMap<Int, Node>()
    private val cache = LinkedNodeList()

    fun get(key: Int): Int {
        if (!map.containsKey(key)) {
            return -1
        }
        return cache.moveToRecent(map[key]!!)
    }

    fun put(key: Int, value: Int) {
        if (map.containsKey(key)) {
            val node = map[key]!!
            node.value = value
            cache.moveToRecent(node)
            return
        }

        if (map.size >= capacity) {
            map.remove(cache.removeOldest())
        }
        map[key] = cache.addRecent(Node(key, value))
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * var obj = LRUCache(capacity)
 * var param_1 = obj.get(key)
 * obj.put(key,value)
 */
fun main() {
    val cache = LRUCache2(2 /* capacity */)
    cache.put(1, 1)
    cache.put(2, 2)
    var v = cache.get(1) // returns 1
    println(v)
    cache.put(3, 3) // evicts key 2
    v = cache.get(2) // returns -1 (not found)
    println(v)
    cache.put(4, 4) // evicts key 1
    v = cache.get(1) // returns -1 (not found)
    println(v)
    v = cache.get(3) // returns 3
    println(v)
    v = cache.get(4) // returns 4
    println(v)
}
