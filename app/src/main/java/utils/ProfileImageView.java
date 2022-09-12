package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;

public class ProfileImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Bitmap bm;

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bm = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bm != null) {
            setImageBitmap(bm);
            bm = null;
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (viewWidth == 0 || viewHeight == 0) {
            this.bm = bm;
            return;
        }
        setScaleType(ScaleType.CENTER_CROP);

        Matrix m = getImageMatrix();
        m.reset();
        int min = Math.min(bm.getWidth(), bm.getHeight());
        int cut;
        if (bm.getWidth() > viewWidth && bm.getWidth() > bm.getHeight()) {
            cut = Math.round((bm.getWidth() - viewWidth) / 2.f);
        } else {
            cut = 0;
        }
        RectF drawableRect = new RectF(cut, 0, min - cut, min);

        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.START);
        setImageMatrix(m);
        super.setImageBitmap(bm);
    }
}