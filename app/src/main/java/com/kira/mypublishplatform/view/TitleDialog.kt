package com.kira.mypublishplatform.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kira.mypublishplatform.R

/**
 * 标题对话框
 */
class TitleDialog : Dialog {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, theme: Int) : super(context!!, theme) {}

    class Builder(private val context: Context) {
        private var title: String? = null
        private var message: String? = null
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var contentView: View? = null
        private var positiveButtonClickListener: DialogInterface.OnClickListener? = null
        private var negativeButtonClickListener: DialogInterface.OnClickListener? = null
        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        fun setMessage(message: Int): Builder {
            this.message = context.getText(message) as String
            return this
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        fun setTitle(title: Int): Builder {
            this.title = context.getText(title) as String
            return this
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setContentView(v: View?): Builder {
            contentView = v
            return this
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        fun setPositiveButton(
            positiveButtonText: Int,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            this.positiveButtonText = context
                .getText(positiveButtonText) as String
            positiveButtonClickListener = listener
            return this
        }

        fun setPositiveButton(
            positiveButtonText: String?,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            this.positiveButtonText = positiveButtonText
            positiveButtonClickListener = listener
            return this
        }

        fun setNegativeButton(
            negativeButtonText: Int,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            this.negativeButtonText = context
                .getText(negativeButtonText) as String
            negativeButtonClickListener = listener
            return this
        }

        fun setNegativeButton(
            negativeButtonText: String?,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            this.negativeButtonText = negativeButtonText
            negativeButtonClickListener = listener
            return this
        }

        fun create(): TitleDialog {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // instantiate the dialog with the custom Theme
            val dialog = TitleDialog(context, R.style.Dialog)
            val layout = inflater.inflate(R.layout.view_order_dialog, null)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.addContentView(layout, params)
            val window = dialog.window
            val attributes = window!!.attributes
            attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes.alpha = 0.9f
            window.attributes = attributes
            window.setGravity(Gravity.CENTER)
            // set the dialog title
            (layout.findViewById<View>(R.id.tv_title) as TextView).text = title
            // set the confirm button
            if (positiveButtonText != null) {
                (layout.findViewById<View>(R.id.tv_btn_sure) as TextView).text = positiveButtonText
                if (positiveButtonClickListener != null) {
                    layout.findViewById<View>(R.id.tv_btn_sure)
                        .setOnClickListener {
                            positiveButtonClickListener!!.onClick(
                                dialog,
                                BUTTON_POSITIVE
                            )
                        }
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById<View>(R.id.tv_btn_sure).visibility = View.GONE
            }
            // set the cancel button
            if (negativeButtonText != null) {
                (layout.findViewById<View>(R.id.tv_btn_cancel) as TextView).text =
                    negativeButtonText
                if (negativeButtonClickListener != null) {
                    layout.findViewById<View>(R.id.tv_btn_cancel)
                        .setOnClickListener {
                            negativeButtonClickListener!!.onClick(
                                dialog,
                                BUTTON_NEGATIVE
                            )
                        }
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById<View>(R.id.tv_btn_cancel).visibility = View.GONE
            }
            // set the content message
            if (message != null) {
                (layout.findViewById<View>(R.id.tv_content) as TextView).text = message
            }
            //            else if (contentView != null) {
//                // if no message set
//                // add the contentView to the dialog body
//                ((LinearLayout) layout.findViewById(R.id.content))
//                        .removeAllViews();
//                ((LinearLayout) layout.findViewById(R.id.content))
//                        .addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//            }
            dialog.setContentView(layout)
            return dialog
        }
    }
}