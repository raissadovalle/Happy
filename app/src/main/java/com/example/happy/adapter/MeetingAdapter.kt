package com.example.happy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.AddMeetingActivity
import com.example.happy.R
import com.example.happy.model.Meeting

class MeetingAdapter(val context: Context) : RecyclerView.Adapter<MeetingAdapter.ViewHolder>() {

    var list: MutableList<Meeting> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.meetings, parent, false)

        return MeetingAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MeetingAdapter.ViewHolder, position: Int) {
        val meeting = list[position]
        holder.subjectMeeting.text = meeting.subject
        holder.verdictMeeting.text = meeting.verdict
        holder.dateMeeting.text = meeting.date
        holder.cardView.setOnClickListener {
            val intent = Intent(context, AddMeetingActivity::class.java)
            intent.putExtra("MEETING", meeting)
            intent.putExtra("IS_EDIT_MEETING", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectMeeting: TextView = itemView.findViewById(R.id.subject_meeting)
        val verdictMeeting: TextView = itemView.findViewById(R.id.verdict_meeting)
        val dateMeeting: TextView = itemView.findViewById(R.id.date_meeting)
        val cardView: CardView = itemView.findViewById(R.id.cv_meetings)
    }
}