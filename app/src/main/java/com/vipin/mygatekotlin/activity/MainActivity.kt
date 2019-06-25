package com.vipin.mygatekotlin.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.vipin.mygatekotlin.R
import com.vipin.mygatekotlin.adapter.RecyclerAdapter
import com.vipin.mygatekotlin.database.DatabaseHelper
import com.vipin.mygatekotlin.model.DataClass

import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val CAMERA_REQUEST = 5555

    lateinit var mRecyclerView: RecyclerView
    private var mRecyclerAdapter: RecyclerAdapter? = null
    lateinit var mList: List<DataClass>
    lateinit var mDataClass: DataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(this)
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            setUpPermission()
        }

        mDataClass = DataClass()
        mList = DatabaseHelper.getInstance(this)!!.allData

        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerAdapter = RecyclerAdapter(mList, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = mRecyclerAdapter
    }

    private fun setUpPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            101
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST) {
            Thread(Runnable {
                val imageData = data?.extras?.get("data") as Bitmap?
                val stream = ByteArrayOutputStream()

                if (imageData == null) return@Runnable

                imageData.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val imageInBytes = stream.toByteArray()
                mDataClass.image = imageInBytes
                mDataClass.randomNum = randomNumber
                DatabaseHelper.getInstance(this)?.addData(mDataClass)
            }).start()
        }
    }

    private val randomNumber: Int
        get() {
            val ran = Random(System.currentTimeMillis())
            return 100000 + ran.nextInt(99999)
        }

    override fun onClick(v: View?) {

        if (v?.id == R.id.fab) {
            intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }

    private fun refreshListView() {
        val runnable = Runnable {
            mList = DatabaseHelper.getInstance(this)!!.allData
            if (mList.isNotEmpty()) {
                mRecyclerAdapter?.setList(mList)
            }
        }
        Handler().post(runnable)
    }
}













