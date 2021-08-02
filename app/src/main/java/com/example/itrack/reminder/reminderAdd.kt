package com.example.itrack.reminder


import android.app.*
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.itrack.R
import com.getbase.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class reminderAdd : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {
    lateinit var mtoolbar: Toolbar
    lateinit var mTitleText: EditText
    lateinit var mTimeText:TextView
    lateinit var mDateText: TextView
    lateinit var mRepeatText: TextView
    lateinit var mRepeatIn: TextView
    lateinit var mRepeatType: TextView
    lateinit var mFab1: FloatingActionButton
    lateinit var mFab2 :FloatingActionButton
    lateinit var mRepeatSwitch: Switch
    lateinit var mCalendar: Calendar
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    var mTouchListener= View.OnTouchListener{ view, motionEvent->
        mReminderChanged = true
        return@OnTouchListener false
    }

    var mYear: Int = 0
    var mMonth : Int = 0
    var mHour : Int = 0
    var mMinute : Int = 0
    var mDay : Int =0
    var mRepeatTime : Long = 0
    var EXISTING_REMINDER_LOADER= 0

    lateinit var mTitle: String
    lateinit var mTime: String
    lateinit var mDate: String
    lateinit var mRepeat: String
    lateinit var mRepeatIn_str: String
    lateinit var mRepeatType_str: String
    lateinit var mActive: String
    var datenTime : Long? = null
    private var mCurrentUri : Uri? = null
    var mReminderChanged = false

    val KEY_TITLE = "title_key"
    val KEY_TIME = "time_key"
    val KEY_DATE = "date_key"
    val KEY_REPEAT = "repeat_key"
    val KEY_REPEAT_NO = "repeat_no_key"
    val KEY_REPEAT_TYPE = "repeat_type_key"
    val KEY_ACTIVE = "title_key"

    val milMinute = 60000L
    val milHour = 3600000L
    val milDay = 86400000L
    val milWeek = 604800000L
    val milMonth = 2592000000L



    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.reminder_add)

            var supportloaderManager: LoaderManager = supportLoaderManager


            val intent = getIntent()
            mCurrentUri = intent.data

            if (intent == null) {
                setTitle("Add Reminder Details")
                invalidateOptionsMenu()
            } else {
                setTitle("Edit Reminder")
                supportloaderManager!!.initLoader(EXISTING_REMINDER_LOADER, null, this);
            }

            mDateText = findViewById(R.id.reminder_date_ET)
            mTimeText = findViewById(R.id.reminder_time_ET)
            mRepeatIn = findViewById(R.id.reminder_repeatIn_Et)
            mRepeatType = findViewById(R.id.reminder_set_repeat_type)
            mRepeatText = findViewById(R.id.reminder_repeat_text)
            mRepeatSwitch = findViewById(R.id.reminder_repeat_view_switch)
            mFab1 = findViewById(R.id.reminder_starred1)
            mFab2 = findViewById(R.id.reminder_starred2)
            mTitleText = findViewById(R.id.reminder_title_ET)

            mActive = "true"
            mRepeat = "true"

            mRepeatType_str = " "
            mCalendar = Calendar.getInstance()
            mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
            mMinute = mCalendar.get(Calendar.MINUTE)
            mMonth = mCalendar.get(Calendar.MONTH) + 1
            mYear = mCalendar.get(Calendar.YEAR)
            mDay = mCalendar.get(Calendar.DATE)
            mTime = mHour.toString() + ":" + mMinute

            if (mDay.toString().length <= 1 && mMonth.toString().length <= 1) {
                var mddaystr = "0$mDay"
                var mmonth = "0$mMonth"
                mDate = mddaystr + "/" + mmonth + "/" + mYear
            } else if (mDay.toString().length > 1 && mMonth.toString().length <= 1) {
                var mmonth = "0$mMonth"
                mDate = mDay.toString() + "/" + mmonth + "/" + mYear
            } else if (mDay.toString().length <= 1 && mMonth.toString().length > 1) {
                var mddaystr = "0$mDay"
                mDate = mddaystr.toString() + "/" + mMonth + "/" + mYear
            }

            mTitleText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mTitle = p0.toString().trim()
                    mTitleText.setError(null)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })


            var loadCursor = contentResolver.query(mCurrentUri!!, null, null, null, null)!!
            if(loadCursor.moveToFirst() && loadCursor != null){
            val titleColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TITLE)
            val dateColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_DATE)
            val timeColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TIME)
            val repeatColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT)
            val repeatNoColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO)
            val repeatTypeColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE)
            val activeColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE)
            val title = loadCursor.getString(titleColumnIndex)
            val date = loadCursor.getString(dateColumnIndex)
            val time = loadCursor.getString(timeColumnIndex)
            val repeat = loadCursor.getString(repeatColumnIndex)
            val repeatNo = loadCursor.getString(repeatNoColumnIndex)
            val repeatType = loadCursor.getString(repeatTypeColumnIndex)
            val active = loadCursor.getString(activeColumnIndex)
            mTitleText.setText(title)
            if (date != null) {
                mDateText.setText(date)
            }else{
                mDateText.setText(mDate)
            }
            if(time != null) {
                mTimeText.setText(time)
            }else{
                mTimeText.setText(mTime)
            }

            if(repeatNo != null){
                mRepeatIn.setText(repeatNo)
                mRepeatIn_str = repeatNo
            }else{
                mRepeatIn_str = 1.toString()
                mRepeatIn.setText(mRepeatIn_str)
            }

            if (repeatType != null){
                mRepeatType.setText(repeatType)
                mRepeatIn_str = repeatType
            }else{
                mRepeatType_str = "Set Repeat Type"
                mRepeatType.setText(mRepeatType_str)
            }
            if(mRepeatIn.text.toString() != null && mRepeatType.text.toString() != null){
                mRepeatText.setText("Every ${mRepeatIn.text.toString()} ${mRepeatType.text.toString()}(s)")}
            else{
                mRepeatText.setText("Repeat Off")
            }
            if (active != null) {
                mActive = active
            }else{
                mActive = "false"
            }
            if (repeat != null) {
                if (repeat.equals("false")) {
                    mRepeatSwitch.setChecked(false)
                    mRepeatText.setText("Off")
                } else if (repeat.equals("true")) {
                    mRepeatSwitch.setChecked(true)
                }
            }else{
                mRepeatText.setText("Off")
            }
        }

        if (mActive.equals("false")) {
            mFab1.visibility = View.VISIBLE
            mFab2.visibility = View.GONE
        } else if (mActive.equals("true")) {
            mFab1.visibility = View.GONE
            mFab2.visibility = View.VISIBLE

        }
//        setSupportActionBar(mtoolbar)
        //      supportActionBar!!.setTitle("Add Reminder")
        //    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //  supportActionBar!!.setHomeButtonEnabled(true)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_TITLE,mTitleText.text)
        outState.putCharSequence(KEY_TIME, mTitleText.text)
        outState.putCharSequence(KEY_DATE, mDateText.text)
        outState.putCharSequence(KEY_REPEAT, mRepeatText.text)
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatIn.text)
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatType.text)
        outState.putCharSequence(KEY_ACTIVE, mActive)
    }

    fun selectFab1(view: View) {
        mFab1.visibility = View.GONE
        mFab2.visibility = View.VISIBLE
        mActive = "true"
    }
    fun selectFab2(view: View) {
        mFab2.visibility = View.GONE
        mFab1.visibility = View.VISIBLE
        mActive = "false"
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
                AlarmReminderContract.AlermReminderEntry()._ID,
                AlarmReminderContract.AlermReminderEntry().KEY_TITLE,
                AlarmReminderContract.AlermReminderEntry().KEY_DATE,
                AlarmReminderContract.AlermReminderEntry().KEY_TIME,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE,
                AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE,
        )
        return androidx.loader.content.CursorLoader(this, AlarmReminderContract.AlermReminderEntry().CONTENT_URI, projection, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data == null || data.count < 1){
            return
        }
        if(data.moveToFirst()){
            val titleColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TITLE)
            val dateColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_DATE)
            val timeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TIME)
            val repeatColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT)
            val repeatNoColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO)
            val repeatTypeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE)
            val activeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE)

            val repeat = data.getString(repeatColumnIndex)
       }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

    fun selectRepeatType(view: View) {
        var items = arrayOf("Minute","Hour","Day","Week","Month")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Type")
        builder.setItems(items,DialogInterface.OnClickListener { _, which ->
            mRepeatType_str = items[which]
            mRepeatType.setText(mRepeatType_str)
            mRepeatText.setText("Every ${mRepeatIn.text.toString()}${mRepeatType.text.toString()}(s)")
        })
        val alert = builder.create()
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTime(view: View) {
        var loadCursor = contentResolver.query(mCurrentUri!!, null, null, null, null)!!
        if(loadCursor.moveToFirst()){
            val timeColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TIME)
            val dateColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_DATE)
            var date = loadCursor.getString(dateColumnIndex)
            var time = loadCursor.getString(timeColumnIndex)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            val str = "$date $time"
            if(time!=null){
                val dateTime  = LocalDateTime.parse(str, formatter)

                var tpd = TimePickerDialog.newInstance(this,
                        dateTime.hour,
                        dateTime.minute,
                false)
                tpd.isThemeDark = false
                tpd.show(getFragmentManager(), "TimepickerDialog")
                datenTime = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
            }  else{
                val cal = Calendar.getInstance()
                var tpd= TimePickerDialog.newInstance( this,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false)
                tpd.isThemeDark=false
                tpd.show(getFragmentManager(),"TimepickerDialog")
                datenTime = cal.timeInMillis
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(view: View) {
        var loadCursor = contentResolver.query(mCurrentUri!!, null, null, null, null)!!
        if(loadCursor.moveToFirst()){
            val timeColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_TIME)
            val dateColumnIndex = loadCursor.getColumnIndex(AlarmReminderContract.AlermReminderEntry().KEY_DATE)
            var date = loadCursor.getString(dateColumnIndex)
            var time = loadCursor.getString(timeColumnIndex)

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val str = "$date $time"

            val cal = Calendar.getInstance()
            var dpd = DatePickerDialog.newInstance(this,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(getFragmentManager(), "Datapickerdialog")

        }
    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        mHour = hourOfDay
        mMinute = minute
        if(minute<10){
            mTime = hourOfDay.toString()+":"+ "0"+ minute
        }else{
            mTime = hourOfDay.toString()+":"+ minute
        }
        mTimeText.setText(mTime)
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mDay = dayOfMonth
        mMonth = monthOfYear+1
        mYear = year

        if(mDay.toString().length <= 1 && mMonth.toString().length <= 1){
            var mddaystr = "0$mDay"
            var mmonth = "0$mMonth"
            mDate = mddaystr+"/"+ mmonth+"/"+mYear
        }else if (mDay.toString().length > 1 && mMonth.toString().length <= 1){
            var mmonth = "0$mMonth"
            mDate = mDay.toString()+"/"+ mmonth+"/"+mYear
        }else if (mDay.toString().length <=1  && mMonth.toString().length >1){
            var mddaystr = "0$mDay"
            mDate = mddaystr.toString()+"/"+mMonth+"/"+mYear
        }else{
            mDate = "$mDay/$mMonth/$mYear"
        }
        mDateText.text = mDate
    }

    fun onSwitchRepeat(view: View) {
        var on = mRepeatSwitch.isChecked()
        if(on){
            mRepeat= "true"
            mRepeatText.setText("Every $mRepeatIn_str $mRepeatType_str (s)")
        }else{
            mRepeat = "false"
            mRepeatText.setText("Off")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_reminder_row,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (mCurrentUri == null){
            val menuItem= menu!!.findItem(R.id.discard_reminder)
            menuItem.setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_reminder->{
                if(mTitleText.text.toString().length == 0){
                    mTitleText.setError("Reminder Title is required")
                }
                else{
                    saveReminder()
                    finish()
                }
                return true
            }
            R.id.discard_reminder->{
                showDeletionConfirmationDialog()
                return true
            }
            android.R.id.home->{
                if(!mReminderChanged){
                    NavUtils.navigateUpFromSameTask(this)
                    return true
                }
                var discardButton = DialogInterface.OnClickListener {
                    dialogInteface, i ->

                    NavUtils.navigateUpFromSameTask(this)
                }
                showUnsavedChangedDialog(discardButton)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showUnsavedChangedDialog(discardButton: DialogInterface.OnClickListener){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Discard changes and quit editing?")
        builder.setPositiveButton("Discard", discardButton)
        builder.setNegativeButton("Keep Editing", DialogInterface.OnClickListener {dialog, id ->
            if(dialog != null){
                dialog.dismiss()
            }
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun showDeletionConfirmationDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete Reminder?")
        builder.setPositiveButton("Delete", DialogInterface.OnClickListener{dialog, id->
            deleteReminder()
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun deleteReminder(){
        if(mCurrentUri != null){
            var rowsDeleted = contentResolver.delete(mCurrentUri!!, null, null)

            if(rowsDeleted == 0){
                Toast.makeText(this, "Error with deleting reminder", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
    fun saveReminder(){
        var values = ContentValues()
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_TITLE,mTitleText.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_DATE, mDateText.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_TIME, mTimeText.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT, mRepeatText.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO, mRepeatIn.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE, mRepeatType.text.toString())
        values.put(AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE, mActive)

        mCalendar.set(Calendar.MONTH, mMonth-1)
        mCalendar.set(Calendar.YEAR, mYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay)
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour)
        mCalendar.set(Calendar.MINUTE, mMinute)
        mCalendar.set(Calendar.SECOND, 0)

        val selectedTimestamp = mCalendar.getTimeInMillis()


        if (mRepeatType.text.toString().equals("Minute")){
            mRepeatTime = mRepeatIn.text.toString().toInt() * milMinute
        }else if(mRepeatType.text.toString().equals("Hour")){
            mRepeatTime = mRepeatIn.text.toString().toInt()*milHour
        }else if(mRepeatType.text.toString().equals("Day")){
            mRepeatTime = mRepeatIn.text.toString().toInt()*milDay
        }else if(mRepeatType.text.toString().toString().equals("Week")){
            mRepeatTime = mRepeatIn.text.toString().toInt()*milWeek
        }else if(mRepeatType.text.toString().equals("Month")){
            mRepeatTime = mRepeatIn.text.toString().toInt()*milMonth
        }

        if(mCurrentUri == null){
            var newUri= contentResolver.insert(AlarmReminderContract.AlermReminderEntry().CONTENT_URI, values)
            if (newUri == null){
                Toast.makeText(this, "Error Saving Reminder", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Reminder Saved", Toast.LENGTH_SHORT).show()
            }
        }else{
            val rowsAffected = contentResolver.update(mCurrentUri!!,values,null,null)

            if (rowsAffected == 0){
                Toast.makeText(this, "Error updating reminder", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show()
            }
        }

        if(mActive.equals("true")){
            if(mRepeat.equals("true")){
                AlarmScheduler.Singleton.setRepeatAlarm(applicationContext, selectedTimestamp,mCurrentUri!!, mRepeatTime)
            }else if (mRepeat.equals("false")){
                AlarmScheduler.Singleton.setAlarm(applicationContext, selectedTimestamp, mCurrentUri!!)
            }
        }

        //Toast.makeText(this, "$mActive   $mRepeatTime" , Toast.LENGTH_SHORT).show()
    }
    fun notification() {
        var notificationChannel: NotificationChannel
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent =  PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        lateinit var builder : Notification.Builder
        val channelId = "i.apps.notifications"
        val description = "Test Notification"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableVibration(false)
            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder (this, channelId)
                    .setContentTitle("Notification")
                    .setContentText("QUEUE IS EMPTY")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_background)
        }
        else{
            builder = Notification.Builder (this)
                    .setContentTitle("Notification")
                    .setContentText("QUEUE IS EMPTY")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_background)
        }
        notificationManager.notify(1234,builder.build())
    }
    fun setRepeatNo(view: View) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enter Number")

        val input= EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        dialog.setView(input)
        dialog.setPositiveButton("Ok", DialogInterface.OnClickListener{_, which->

            if(input.text.toString().length==0){
                mRepeatIn_str = 1.toString()
                mRepeatIn.setText(mRepeatIn_str)
                mRepeatText.setText("Every $mRepeatIn_str $mRepeatType_str(s)")
            }else{
                mRepeatIn_str = input.text.toString().trim()
                mRepeatIn.setText(mRepeatIn_str)
                mRepeatText.setText("Every $mRepeatIn_str $mRepeatType_str(s)")
            }
        })
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener{_, which->
        })
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
