package talent.jump.utility

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.skydoves.powerspinner.PowerSpinnerView
import talent.jump.R
import talent.jump.model.ApiTokenClient

class TuberInterfaceDialog(ctx:Context): BottomSheetDialog(ctx) {

    lateinit var closeBtn:ImageView
    lateinit var startlive:ConstraintLayout
    lateinit var roomName:EditText
    lateinit var roomType:Spinner
    var apiclient=ApiTokenClient()

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

        roomName=findViewById(R.id.room_name_editor)!!
        roomType=findViewById(R.id.type_name_editor)!!

        closeBtn= findViewById(R.id.icon_close)!!
        closeBtn.setOnClickListener {
            dismiss()
        }
        val adapter = ArrayAdapter.createFromResource(context, R.array.roomtype, android.R.layout.simple_spinner_dropdown_item)
        roomType.adapter = adapter
        startlive=findViewById(R.id.start_live_btn)!!
        startlive.setOnClickListener {

            /**
             *
             *     "display_name": "Oiltest",
            "description": "油王直播",
            "type_id": 1,
            "private": true,
            "pass_code": "abc123",
            "status": 1,
            "living_at": 0
             */

            val createJson= JsonObject()
            createJson.addProperty("display_name","${roomName.text}")
            createJson.addProperty("description","${roomType.selectedItem}")
            createJson.addProperty("type_id",2)
            createJson.addProperty("private",true)
            createJson.addProperty("pass_code","abc123")
            createJson.addProperty("status",1)
            createJson.addProperty("living_at",0)
            apiclient.createStreams(createJson)
            dismiss()
        }


    }

}