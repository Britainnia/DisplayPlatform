package com.kira.mypublishplatform.utils

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File


class DownloadUtils(  //上下文
    private val mContext: Context
) {
    //下载器
    private var downloadManager: DownloadManager? = null

    //下载的ID
    private var downloadId: Long = 0
    fun downloadApk(downLoadUrl: String, description: String) {
        val apkName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1)
        Logger.d(
            String.format(
                "DownLoadUrl: %s \nDownLoadDescription: %s",
                downLoadUrl,
                description
            )
        )
        val request: DownloadManager.Request
        request = try {
            DownloadManager.Request(Uri.parse(downLoadUrl))
        } catch (e: Exception) {
            Logger.d(String.format("downLoad failed :%s", e.localizedMessage))
            return
        }
        request.setTitle("吉贝云正在升级。。。")
        request.setDescription(description)

        //在通知栏显示下载进度
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        //设置保存下载apk保存路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName)

        //获取DownloadManager
        downloadManager =
            mContext.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager!!.enqueue(request)
        }

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    //广播监听下载的各个状态
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
            val completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val downloadFileUri = downloadManager!!.getUriForDownloadedFile(completeDownloadId)

            installApk(downloadFileUri, context)
//            }
        }
    }

    private fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                null,
                null,
                null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    private fun installApk(downLoadApkUri: Uri?, context: Context) {
        if (downLoadApkUri == null) {
            Logger.e("Download apk failed,empty apk uri")
            return
        } else {
            Logger.d(String.format("Download apk finish ,apkUri:%s", downLoadApkUri.toString()))
        }

        //获取待安装的apk文件
        val apkFile = File(getRealFilePath(context, downLoadApkUri))
        if (!apkFile.exists()) {
            Logger.d("Apk file is not exist.")
            return
        }

        //调用系统安装apk
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //7.0版本以上
            val uriForFile =
                FileProvider.getUriForFile(
                    context,
                    "com.kira.mypublishplatform.fileProvider",
                    apkFile
                )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive")
            Logger.d(String.format("Install apk,\ndata: %s", uriForFile))
        } else {
            val uri = Uri.fromFile(apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            Logger.d(String.format("Install apk,\ndata: %s", uri))
        }
        try {
            context.startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
            Logger.e(
                String.format(
                    "Start system install activity exception: %s",
                    e.localizedMessage
                )
            )
        }
    }

    companion object {
        private const val TAG = "DownloadUtils"
    }
}