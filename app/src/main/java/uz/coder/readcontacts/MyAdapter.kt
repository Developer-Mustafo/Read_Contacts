package uz.coder.readcontacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.coder.readcontacts.databinding.ItemContactBinding

class MyAdapter:ListAdapter<Contact,MyAdapter.VH>(DiffUtil())
{
    inner class VH(private val binding: ItemContactBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(contact: Contact){
            binding.apply {
                name.text = contact.name
                phoneNumber.text = contact.phone
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(ItemContactBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}