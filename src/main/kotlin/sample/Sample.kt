package sample

// https://speakerdeck.com/ntaro/chu-xue-zhe-xiang-ke-kotlindezienerikusuwoxue-bou

fun c1(args: Array<String>) {
    class Box(val value: Any)

    val box1: Box = Box("hello")
    val box2: Box = Box(3)

    repeat(box2.value as Int) {
        val message: String = box1.value as String
        println(message.toUpperCase())
        // キャストは危険
    }

    // あらゆる型に対応させたい ＝ 固定の型で定義したく無い
    // ジェネリクス！
}

fun c2(args: Array<String>) {
    // ジェネリッククラス
    // 型パラメータ（仮の型）が宣言されているクラス
    class Box<T>(val value: T)

    // 型引数を指定する
    val box1: Box<String> = Box<String>("hello")
    val box2: Box<Int> = Box<Int>(3)

    repeat(box2.value) {
        val message: String = box1.value
        println(message.toUpperCase())
    }

    // 型パラメータ・型引数
    // parameter / argument
}

fun c3(args: Array<String>) {
    // 変位 variance
    class Box<T>(val value: T)

    // 不変 invariant
    // （非変）
    //  サブタイプの関係が成り立たない
    val box1: Box<Int> = Box(123)
    //val box2: Box<Number> = box1 // コンパイルエラー (Number <- Int だが）

    fun toInt(box: Box<Number>): Int = box.value.toInt()

    //toInt(Box<Float>(12.34f)) // コンパイルエラー


    // 共変 covariant
    // サブタイピング関係が成り立つ
    val box2: Box<out Number> = box1 // outキーワードを用いる

    // 型プロジェクション projection
    // 型のある側面にだけ注目することで、サブタイピングの関係を変更できる
    class MutableBox<T>(var value: T)
    val mbox1: MutableBox<Int> = MutableBox(123)
    val mbox2: MutableBox<out Number> = mbox1

    //mbox2.value = 0.5  // コンパイルエラー。Numberのサブタイプでも入力できない（実態がIntなので。危険）


    // 反変 contravariant
    // 型パラメータと逆のサブタイピング関係が成り立つ
    fun setDefault(box: MutableBox<in Int>) {
        box.value = 0
    }

    val box: MutableBox<Number> = MutableBox(Double.NaN)
    setDefault(box)
    println(box.value)

    // 不変 -> キーワード：なし 可能な操作：入出力
    // 共変 -> キーワード：out 可能な操作：出力
    // 反変 -> キーワード：in 可能な操作：入力
}

fun c4(args: Array<String>) {
    // 変位の指定
    class TBox<T>(val value: T)

    // ジェネリック型を使う際に指定するので使用場所変位指定ということもある
    val tbox1: TBox<Int> = TBox(123)
    val tbox2: TBox<out Number> = tbox1

    // 宣言場所変位指定
    class Box<out T>(val value: T)
    val box1: Box<Int> = Box(123)
    val box2: Box<Number> = box1
}

fun c5(args: Array<String>) {
    // ジェネリック制約

    class NumberBox<out T: Number>(val value:T) {
        fun toInt(): NumberBox<Int> = NumberBox(value.toInt())
    }

    //val box1: NumberBox<String> = NumberBox("123")// エラー

    // 複数の上限境界

}

fun c6(args: Array<String>) {
    // 型消去とreified型

    // 型消去
    class Box<T>(val value: T)
    val box: Box<String> = Box("hello")
    val value: String = box.value

    // Javaだと
    //Box box = new Box("hello");
    //String value = (string)box.getValue();

    //コンパイルすると型引数が消える
    //型安全性の面のみで活用される

    val my1: Any = Box<Int>(123)
    ///if (my1 is Box<Int>) {   // エラー
    //
    //}

    if(my1 is Box<*>) { // スタープロジェクション

    }
    // スタープロジェクション
    // 型が決まっているが不明な時 or 興味がないときに使用する
    // <out Any?> and <in Nothing>的に振る舞う

    val list : MutableList<*> = mutableListOf<Int>()
    val first: Any? = list.get(0)
    //list.add(123) // エラー
}

// 具象型 reified
inline fun <reified T> Any.isA(): Boolean = this is T

fun c7(args: Array<String>) {

    println(5.isA<Int>())
    println(5.isA<String>())
}
