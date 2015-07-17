package wxm.com.androiddesign;

/**
 * Created by hd_chen on 2015/7/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**

 */
public class MatrixImageView extends ImageView {
    private final static String TAG = "MatrixImageView";
    private GestureDetector mGestureDetector;
    private Matrix mMatrix = new Matrix();
    private float mImageWidth;
    private float mImageHeight;
    private boolean shouldReset = false;

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MatrixTouchListener mListener = new MatrixTouchListener();
        setOnTouchListener(mListener);
        mGestureDetector = new GestureDetector(getContext(), new GestureListener(mListener));
        setBackgroundColor(Color.BLACK);
        setScaleType(ScaleType.FIT_CENTER);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        // TODO Auto-generated method stub
        super.setImageBitmap(bm);
        mMatrix.set(getImageMatrix());
        float[] values = new float[9];
        mMatrix.getValues(values);
        mImageWidth = getWidth() / values[Matrix.MSCALE_X];
        mImageHeight = (getHeight() - values[Matrix.MTRANS_Y] * 2) / values[Matrix.MSCALE_Y];
    }

    public class MatrixTouchListener implements OnTouchListener {
        private static final int MODE_DRAG = 1;
        private static final int MODE_ZOOM = 2;
        private static final int MODE_UNABLE = 3;
        float mMaxScale = 6;
        float mDobleClickScale = 2;
        private int mMode = 0;//
        private float mStartDis;
        private Matrix mCurrentMatrix = new Matrix();
        private PointF startPoint = new PointF();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mMode = MODE_DRAG;
                    startPoint.set(event.getX(), event.getY());
                    isMatrixEnable();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    reSetMatrix();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMode == MODE_ZOOM) {
                        setZoomMatrix(event);
                    } else if (mMode == MODE_DRAG) {
                        setDragMatrix(event);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (mMode == MODE_UNABLE) return true;
                    mMode = MODE_ZOOM;
                    mStartDis = distance(event);
                    break;
                default:
                    break;
            }

            return mGestureDetector.onTouchEvent(event);
        }

        public void setDragMatrix(MotionEvent event) {
            if (isZoomChanged()) {
                float dx = event.getX() - startPoint.x;
                float dy = event.getY() - startPoint.y;
                if (Math.sqrt(dx * dx + dy * dy) > 10f) {
                    startPoint.set(event.getX(), event.getY());

                    mCurrentMatrix.set(getImageMatrix());
                    float[] values = new float[9];
                    mCurrentMatrix.getValues(values);
                    dx = checkDxBound(values, dx);
                    dy = checkDyBound(values, dy);
                    mCurrentMatrix.postTranslate(dx, dy);
                    setImageMatrix(mCurrentMatrix);
                }
            }
        }

        private boolean isZoomChanged() {
            float[] values = new float[9];
            getImageMatrix().getValues(values);

            float scale = values[Matrix.MSCALE_X];

            mMatrix.getValues(values);
            return scale != values[Matrix.MSCALE_X];
        }

        private float checkDyBound(float[] values, float dy) {
            float height = getHeight();
            //  mImageHeight=(getHeight()-values[Matrix.MTRANS_Y]*2)/values[Matrix.MSCALE_Y];
//            if(mImageHeight*values[Matrix.MSCALE_Y]<height)
            if (values[Matrix.MSCALE_Y] <= 1)
                return 0;
            if (values[Matrix.MTRANS_Y] + dy > 0)
//                dy=-values[Matrix.MTRANS_Y];
                return 0;
            else if (values[Matrix.MTRANS_Y] + dy < -(getHeight() * values[Matrix.MSCALE_Y] - height))
                // dy=-(mImageHeight*values[Matrix.MSCALE_Y]-height)-values[Matrix.MTRANS_Y];
                return 0;
            return dy;
        }

        private float checkDxBound(float[] values, float dx) {
            float width = getWidth();
            //  mImageWidth=getWidth()/values[Matrix.MSCALE_X];
//            if(mImageWidth*values[Matrix.MSCALE_X]<width)
            if (values[Matrix.MSCALE_X] <= 1)
                return 0;
            if (values[Matrix.MTRANS_X] + dx > 0)
//                dx=-values[Matrix.MTRANS_X];
                return 0;
            else if (values[Matrix.MTRANS_X] + dx < -(getWidth() * values[Matrix.MSCALE_X] - width))
//                dx=-(mImageWidth*values[Matrix.MSCALE_X]-width)-values[Matrix.MTRANS_X];
                return 0;
            return dx;
        }

        private void setZoomMatrix(MotionEvent event) {

            if (event.getPointerCount() < 2) return;
            float endDis = distance(event);
            if (endDis > 10f) {
                float scale = endDis / mStartDis;
                mStartDis = endDis;
                mCurrentMatrix.set(getImageMatrix());
                float[] values = new float[9];
                mCurrentMatrix.getValues(values);

                scale = checkMaxScale(scale, values);
                setImageMatrix(mCurrentMatrix);
            }
        }

        private float checkMaxScale(float scale, float[] values) {
            if (scale * values[Matrix.MSCALE_X] > mMaxScale)
                scale = mMaxScale / values[Matrix.MSCALE_X];
            mCurrentMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            if (values[Matrix.MTRANS_X] > 0 || values[Matrix.MTRANS_Y] > 0 ||
                    values[Matrix.MTRANS_X] < -(getWidth() * values[Matrix.MSCALE_X] - getWidth()) ||
                    values[Matrix.MTRANS_Y] < -(getHeight() * values[Matrix.MSCALE_Y] - getHeight())) {
                shouldReset = true;
                return 1;
            }
            return scale;
        }

        private void reSetMatrix() {
            if (checkRest() || shouldReset) {
                shouldReset = false;
                mCurrentMatrix.set(mMatrix);
                setImageMatrix(mCurrentMatrix);
            }
        }

        private boolean checkRest() {
            // TODO Auto-generated method stub
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            float scale = values[Matrix.MSCALE_X];
            mMatrix.getValues(values);
            return scale < values[Matrix.MSCALE_X];
        }

        private void isMatrixEnable() {
            if (getScaleType() != ScaleType.CENTER) {
                setScaleType(ScaleType.MATRIX);
            } else {
                mMode = MODE_UNABLE;
            }
        }

        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        public void onDoubleClick() {
            float scale = isZoomChanged() ? 1 : mDobleClickScale;
            mCurrentMatrix.set(mMatrix);//��ʼ��Matrix
            mCurrentMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mCurrentMatrix);
        }
    }


    private class GestureListener extends SimpleOnGestureListener {
        private final MatrixTouchListener listener;

        public GestureListener(MatrixTouchListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            listener.onDoubleClick();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onShowPress(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapConfirmed(e);
        }

    }


}
