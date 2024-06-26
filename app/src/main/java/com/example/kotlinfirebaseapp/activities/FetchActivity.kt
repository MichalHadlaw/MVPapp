package com.example.kotlinfirebaseapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaseapp.R
import com.example.kotlinfirebaseapp.adapters.DrugAdapter
import com.example.kotlinfirebaseapp.models.DrugModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchActivity : AppCompatActivity() {

    private lateinit var drugRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var drugList: ArrayList<DrugModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)

        drugRecyclerView = findViewById(R.id.rvDrugs)
        drugRecyclerView.layoutManager = LinearLayoutManager(this)
        drugRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        drugList = arrayListOf<DrugModel>()

        getDrugsData()

    }

    private  fun getDrugsData(){
        drugRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Drugs")

        dbRef.addValueEventListener(object  : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
               drugList.clear()
                if (snapshot.exists()){
                    for (drugSnap in snapshot.children){
                        val drugData = drugSnap.getValue(DrugModel::class.java)
                        drugList.add(drugData!!)
                    }

                    val dAdapter = DrugAdapter(drugList)
                    drugRecyclerView.adapter = dAdapter

                    dAdapter.setOnItemClickListener(object : DrugAdapter.onItemClickListener{
                        override fun onItemCick(position: Int) {
                           val intent = Intent(this@FetchActivity, DrugsDetailActivity::class.java)

                           //put extras
                           intent.putExtra("drugId", drugList[position].drugId)
                           intent.putExtra("drugName", drugList[position].DrugName)
                            intent.putExtra("doze", drugList[position].Doze)
                            intent.putExtra("DozeTimes", drugList[position].DrugCount)
                            startActivity(intent)
                        }

                    })

                    drugRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Error: ${error.message}, Details: ${error.details}")
            }

        })
    }
}