package com.example.test


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test.PCcafe.pc_3pop_changdongActivity.pc_3pop_changdongActivity
import com.example.test.PCcafe.pc_3pop_kwangwoon_univActivity.pc_3pop_kwangwoon_univActivity
import com.example.test.PCcafe.pc_ntop_kwangwoon_univActivity.pc_ntop_kwangwoon_univActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.menual_page.*

class menual : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val TAG : String = "menual"
    var myUid=mAuth.currentUser?.uid.toString()
    var time3: Long = 0
    var pc_check=10000
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            finishAffinity()
            System.runFinalization()
            System.exit(0)
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   //     val socket = Socket("www.google.com", 80)
   //     val localAddr = socket.getLocalAddress().getHostAddress()
   //     Log.d(FragmentActivity.TAG, "local address is : $localAddr")
    //    Toast.makeText(applicationContext, localAddr.toInt().toString(),Toast.LENGTH_SHORT).show()


        if(myUid.equals("null")){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if(myUid.equals(mAuth.currentUser?.uid.toString())==false){
            myUid=mAuth.currentUser?.uid.toString()
        }

        setContentView(R.layout.menual_page)

        //툴바 추가
        val actionBar = supportActionBar

        actionBar!!.title = "Home"

       // actionBar.setDisplayHomeAsUpEnabled(true)
        //actionBar.setDisplayHomeAsUpEnabled(true)
        // 툴바 추가 완료


        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("member")

        myRef.child(myUid).child("PCcafe").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다
                val value = p0?.value
                pc_cafe.text = "$value"
            }
        })
        myRef.child(myUid).child("local").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다
                val value = p0?.value
                local.text = "$value"
            }
        })
        myRef.child(myUid).child("point").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다
                val value = p0?.value
                point.text = "$value"
            }
        })

        charge.setOnClickListener {
            val intent = Intent(this, ChargeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        info.setOnClickListener {
            val intent = Intent(this, My_infoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        list.setOnClickListener {
            val intent = Intent(this, My_pc_listActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        PC_cafe.setOnClickListener {
            val intent = Intent(this, Search_pc_cafeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        seat.setOnClickListener {
            if(pc_cafe.text.toString().equals("3POP")) {
                if(local.text.toString().equals("광운대")) {
                    val intent = Intent(this, pc_3pop_kwangwoon_univActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else if(local.text.toString().equals("창동")) {
                    val intent = Intent(this, pc_3pop_changdongActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            } else if(pc_cafe.text.toString().equals("NTop")) {
                if(local.text.toString().equals("광운대")) {
                    val intent = Intent(this, pc_ntop_kwangwoon_univActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                }
            } else{
                Toast.makeText(baseContext, "PC cafe를 먼저 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        pc_logout.setOnClickListener {
            System.runFinalization()
            val pcRef : DatabaseReference = database.getReference("PCcafe").child(pc_cafe.text.toString()).child(local.text.toString())

            myRef.child(myUid).child("seat_using").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다
                    val value = p0?.value
                    if(pc_check==10000 && value.toString().equals("")==false) {
                        pcRef.child("$value").child("using").setValue("X")
                        pcRef.child("$value").child("uid").setValue("")
                        pcRef.child("$value").child("time_start").setValue("")
                        myRef.child(myUid).child("seat_using").setValue("")
                        Toast.makeText(baseContext, "pc 로그아웃", Toast.LENGTH_SHORT).show()
                        pc_check=0
                    }else {
                        if("$value".equals("")==false) {
                            pc_check = value.toString().toInt()
                        }
                    }
                }
            })

            if(pc_check!= 10000 && pc_check!=0) {
                pcRef.child(pc_check.toString()).child("using").setValue("X")
                pcRef.child(pc_check.toString()).child("uid").setValue("")
                myRef.child(myUid).child("seat_using").setValue("")
                Toast.makeText(baseContext, "pc 로그아웃", Toast.LENGTH_SHORT).show()
            }
            a.setText(pc_check.toString())
        }

        logout.setOnClickListener {

            mAuth.signOut()
            Toast.makeText(baseContext, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }



}

