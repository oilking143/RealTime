package talent.jump.utility

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import talent.jump.R

class TuberInterfaceDialog(ctx:Context): BottomSheetDialog(ctx) {

    lateinit var closeBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null)

        setContentView(view)
        this.window!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            .setBackgroundResource(android.R.color.transparent)
        if (this.window != null) {
            val params: WindowManager.LayoutParams = this!!.window!!.attributes
            params.dimAmount = 0.0f
            this!!.window!!.attributes = params
        }

        closeBtn= findViewById(R.id.icon_close)!!
        closeBtn.setOnClickListener {

            dismiss()

        }


    }

}