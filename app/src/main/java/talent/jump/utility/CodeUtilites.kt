package talent.jump.utility

import java.lang.StringBuilder
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class CodeUtilites {
    private val CHARS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    )

    private var mCodeUtils: CodeUtilites? = null
    private var mPaddingLeft = 0
    private  var mPaddingTop:Int = 0
    private val mBuilder = StringBuilder()
    private val mRandom: Random = Random()

    //Default Settings
    private val DEFAULT_CODE_LENGTH = 4 //驗證碼的長度  這裡是4位

    private val DEFAULT_FONT_SIZE = 60 //字型大小

    private val DEFAULT_LINE_NUMBER = 3 //多少條幹擾線

    private val BASE_PADDING_LEFT = 20 //左邊距

    private val RANGE_PADDING_LEFT = 35 //左邊距範圍值

    private val BASE_PADDING_TOP = 50 //上邊距

    private val RANGE_PADDING_TOP = 50 //上邊距範圍值

    private val DEFAULT_WIDTH = 200 //預設寬度.圖片的總寬

    private val DEFAULT_HEIGHT = 100 //預設高度.圖片的總高

    private val DEFAULT_COLOR = 0xDF //預設背景顏色值

    private var codeStr=""


    fun getInstance(): CodeUtilites? {
        if (mCodeUtils == null) {
            mCodeUtils = CodeUtilites()
        }
        return mCodeUtils
    }

    //生成驗證碼圖片
    fun createBitmap(): Bitmap? {
        mPaddingLeft = 0 //每次生成驗證碼圖片時初始化
        mPaddingTop = 0
        val bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        //生成的驗證碼
        val code = createCode()
        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR))
        val paint = Paint()
        paint.setTextSize(DEFAULT_FONT_SIZE.toFloat())
        for (i in 0 until code.length) {
            randomTextStyle(paint)
            randomPadding()
            canvas.drawText(code[i].toString() + "", mPaddingLeft.toFloat(),
                mPaddingTop.toFloat(), paint)
        }

        //干擾線
        for (i in 0 until DEFAULT_LINE_NUMBER) {
            drawLine(canvas, paint)
        }
        canvas.save() //儲存
        canvas.restore()
        return bitmap
    }

    //生成驗證碼
    fun createCode(): String {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空內容
        for (i in 0 until DEFAULT_CODE_LENGTH) {
            mBuilder.append(CHARS[mRandom.nextInt(CHARS.size)])
        }
        codeStr=mBuilder.toString()
        return codeStr
    }

    fun getCode():String{
        return codeStr
    }

    //生成干擾線
    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = mRandom.nextInt(DEFAULT_WIDTH)
        val startY = mRandom.nextInt(DEFAULT_HEIGHT)
        val stopX = mRandom.nextInt(DEFAULT_WIDTH)
        val stopY = mRandom.nextInt(DEFAULT_HEIGHT)
        paint.setStrokeWidth(1F)
        paint.setColor(color)
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    //隨機顏色
    private fun randomColor(): Int {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空內容
        var haxString: String
        for (i in 0..2) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF))
            if (haxString.length == 1) {
                haxString = "0$haxString"
            }
            mBuilder.append(haxString)
        }
        return Color.parseColor("#$mBuilder")
    }

    //隨機文字樣式
    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.setColor(color)
        paint.setFakeBoldText(mRandom.nextBoolean()) //true為粗體，false為非粗體
        var skewX = (mRandom.nextInt(11) / 10).toFloat()
        skewX = if (mRandom.nextBoolean()) skewX else -skewX
        paint.setTextSkewX(skewX) //float型別引數，負數表示右斜，整數左斜
        //        paint.setUnderlineText(true); //true為下劃線，false為非下劃線
//        paint.setStrikeThruText(true); //true為刪除線，false為非刪除線
    }

    //隨機間距
    private fun randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT)
        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP)
    }
}