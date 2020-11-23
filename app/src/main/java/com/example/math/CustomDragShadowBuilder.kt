package com.example.math

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.view.View.DragShadowBuilder


class CustomDragShadowBuilder(var v: View) : DragShadowBuilder(v) {
    override fun onDrawShadow(canvas: Canvas) {
        super.onDrawShadow(canvas)

        /*Modify canvas if you want to show some custom view that you want
      to animate, that you can check by putting a condition passed over
      constructor. Here I'm taking the same view*/
        canvas.drawBitmap(getBitmapFromView(v), 0f, 0f, null)
    }

    override fun onProvideShadowMetrics(shadowSize: Point, touchPoint: Point) {
/*Modify x,y of shadowSize to change the shadow view
   according to your requirements. Here I'm taking the same view width and height*/
        shadowSize[v.width] = v.height
        /*Modify x,y of touchPoint to change the touch for the view
 as per your needs. You may pass your x,y position of finger
 to achieve your results. Here I'm taking the lower end point of view*/
        touchPoint[v.width/2] = v.height
    }

    private fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }
}