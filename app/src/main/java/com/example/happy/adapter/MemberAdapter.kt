package com.example.happy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.MembersActivity
import com.example.happy.R

import com.example.happy.model.Member

class MemberAdapter(val context: Context) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    var list: List<Member> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.members, parent, false)

        return MemberAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {
        val member = list[position]
        holder.nameMember.text = member.name
        holder.celphoneMember.text = member.celNumber
        holder.emailMember.text = member.email
        holder.imageView.setImageResource(R.drawable.ic_baseline_attach_money_24) //TODO colocar foto de perfil
        holder.cardView.setOnClickListener {
            val intent = Intent(context, MembersActivity::class.java)
            intent.putExtra("MEMBER", member)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_member)
        val nameMember: TextView = itemView.findViewById(R.id.name_member)
        val celphoneMember: TextView = itemView.findViewById(R.id.celphone_member)
        val emailMember: TextView = itemView.findViewById(R.id.email_member)
        val cardView: CardView = itemView.findViewById(R.id.cv_member)
    }
}