package com.xuanfeng.testcomponent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;


//文字溢出省略号处理
public class OverFlowTextView extends TextView {
    public OverFlowTextView(Context context) {
        this(context,null);
    }

    public OverFlowTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OverFlowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tempContent = "阿斯顿放假啊都说了；会计法阿斯顿法拉第口角是非圣诞节发是的；费拉达斯电视剧啊疯了；";
        setText(tempContent + getTail());
        processText();
    }



    private String tempContent;//截取内容临时变量
    private float singleMeasureWidth;//单行测量宽度

    private void processText() {

        post(new Runnable() {
            @Override
            public void run() {

                float first = getLayout().getLineWidth(0);
                float second = getLayout().getLineWidth(1);
                float third = getLayout().getLineWidth(2);
                singleMeasureWidth = getPaint().measureText(tempContent + getTail());

                if ((first + second + third) == singleMeasureWidth) {//没有溢出文字
                    return;
                }

                //处理溢出
                int viewWidth = getMeasuredWidth();
                float firstSpace = viewWidth - first;
                float secondSpace = viewWidth - second;
                while (singleMeasureWidth >= (viewWidth * 3 - firstSpace - secondSpace)) {
                    tempContent = tempContent.substring(0, tempContent.length() - 1);
                    singleMeasureWidth = getPaint().measureText(tempContent + getTail());
                }
                setText(tempContent + getTail());



            }
        });
    }

    private String getTail() {
        return "...等5件商品" ;
    }
}
