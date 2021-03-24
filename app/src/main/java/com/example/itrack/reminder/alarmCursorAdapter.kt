package com.example.itrack.reminder

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.itrack.R



class alarmCursorAdapter(context: Context, cursor: Cursor?): CursorAdapter(context,cursor) {
    companion object {
        lateinit var mTitleText: TextView
        lateinit var mDateandTimeText: TextView
        lateinit var mRepeatInfoText: TextView
        lateinit var mActiveImage: ImageView
    }
    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
        return  LayoutInflater.from(p0).inflate(R.layout.row_alarm,p2,false)
    }

    override fun bindView(p0: View, p1: Context, p2: Cursor) {
        mTitleText = p0.findViewById(R.id.reminder_rec_title)
        mDateandTimeText = p0.findViewById(R.id.reminder_rec_dnt)
        mRepeatInfoText = p0.findViewById(R.id.reminder_rec_repeatInfo)
        mActiveImage = p0.findViewById(R.id.imageView_active)
        val titleColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TITLE)
        val dateColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_DATE)
        val timeColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TIME)
        val repeatColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT)
        val repeatNoColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO)
        val repeatTypeColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE)
        val activeColumnIndex = p2.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE)

        val title = p2.getString(titleColumnIndex)
        val date = p2.getString(dateColumnIndex)
        val time = p2.getString(timeColumnIndex)
        val repeat = p2.getString(repeatColumnIndex)
        val repeatNo = p2.getString(repeatNoColumnIndex)
        val repeatType = p2.getString(repeatTypeColumnIndex)
        val active = p2.getString(activeColumnIndex)

        val dateTime = date+ " "+ time

        mTitleText.text=title

        if(date != null){
            val dateTime = date +" "+ time
            setReminderDateTime(dateTime)
        }else{
            mDateandTimeText.setText("Date not set")
        }

        if(repeat != null){
            setReminderRpeatInfo(repeat,repeatNo,repeatType)
        }else{
            mRepeatInfoText.setText("Repeat not set")
        }

        if (active != null){
            setActiveImage(active)
        }else{
            mActiveImage.setImageResource(R.drawable.ic_action_nonotif)
        }
    }

   // fun setReminderTitle(title: String){
     //   mTitleText.setText(title)
       // var letter = "A"
        //if (title!=null && title.isEmpty()){
          //  letter = title.substring(0,1)
        //}
        //var color = mColorGenerator().getRandomColor()
        //var mDrawableBuilder = TextDrawable.builder().buildRound(letter, color)
    //}

    fun setReminderDateTime(dnt: String){
        mDateandTimeText.setText(dnt)
    }

    fun setReminderRpeatInfo(repeat:String, repeanNo:String, repeatType:String){
        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every "+ repeanNo+" "+repeatType+"(s)")
        }else if(repeat.equals("false")){
            mRepeatInfoText.setText("Repeat Off")
        }
    }
    fun setActiveImage(active: String){
        if(active.equals("true")){
            mActiveImage.setImageResource(R.drawable.ic_action_notif2)
        }else if(active.equals("false")){
            mActiveImage.setImageResource(R.drawable.ic_action_nonotif)
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

}