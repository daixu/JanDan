package com.daixu.jandan.view.img

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.daixu.jandan.R
import com.daixu.jandan.base.BaseActivity
import com.daixu.jandan.utils.GlideUtil
import com.github.chrisbanes.photoview.OnPhotoTapListener
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.item_image_detail.view.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class ImageDetailActivity : BaseActivity(), ViewPager.OnPageChangeListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private var list: ArrayList<String>? = null
    private var mCurPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        list = intent.getStringArrayListExtra("url")
        mCurPosition = intent.getIntExtra("position", 0)

        if (list!!.size == 1) {
            tv_index.visibility = View.GONE
        }

        view_pager.adapter = ImagePagerAdapter(list!!)
        view_pager.addOnPageChangeListener(this)
        view_pager.currentItem = mCurPosition!!
        onPageSelected(mCurPosition!!)

        img_down.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private val PERMISSION_ID = 0x0001

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_down -> {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (EasyPermissions.hasPermissions(this@ImageDetailActivity, *permissions)) {
                    saveToFile()
                } else {
                    EasyPermissions.requestPermissions(this@ImageDetailActivity, "请授予保存图片权限", PERMISSION_ID, *permissions)
                }
            }
            else -> {
            }
        }
    }

    private fun saveToFile() {
        val path = list!![mCurPosition!!]

        GlideUtil.saveToFile(this@ImageDetailActivity, path, view_pager)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        saveToFile()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, R.string.gallery_save_file_not_have_external_storage_permission, Toast.LENGTH_SHORT).show()
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        tv_index.text = String.format("%s/%s", position + 1, list!!.size)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    internal inner class ImagePagerAdapter constructor(private val mList: List<String>) : PagerAdapter() {

        override fun getCount(): Int {
            return mList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val view = View.inflate(container.context, R.layout.item_image_detail, null)
            val photoView = view.img_max_src

            val url = mList[position]
            if (url.endsWith("gif") || url.endsWith("GIF")) {
                GlideUtil.loadPictureGif(container.context, url, photoView)
            } else {
                GlideUtil.loadBitmap(container.context, url, photoView)
            }

            container.setBackgroundColor(Color.BLACK)
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            photoView.setOnPhotoTapListener(PhotoTapListener())
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

    private inner class PhotoTapListener : OnPhotoTapListener {

        override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
            finish()
        }
    }
}
