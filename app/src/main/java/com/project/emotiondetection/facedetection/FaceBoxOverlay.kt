package com.project.emotiondetection.facedetection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil


/*
*   An overlay in Android development is often used to display additional graphical elements on top of existing views or canvases without altering the underlying content.
*
*   The FaceBoxOverlay class serves as such an overlay. Its main purpose is to facilitate the drawing of face bounding boxes by using canvas
*
*   Face Bounding Boxes: In the context of face detection or recognition algorithms, a bounding box is a rectangular region that encloses a detected face.
*   These boxes help visualize the location and size of detected faces within an image or video frame.
*
*   Drawing on Canvas: Android provides the Canvas class for drawing graphics on a surface, such as a View or a Bitmap.
*   The FaceBoxOverlay class utilizes a Canvas object to draw face bounding boxes.
*/

open class FaceBoxOverlay(context: Context?,attrs:AttributeSet?):View(context,attrs) {
    //AttributeSet: It includes attributes like android:layout_width, android:layout_height, and any custom attributes defined in the view's XML declaration.
    private val lock = Any()
    private val faceBoxes:MutableList<FaceBox> = mutableListOf()
    var mScale:Float? = null
    var mOffsetX:Float? = null
    var mOffsetY:Float? = null

    abstract class FaceBox(private val overlay: FaceBoxOverlay){
        abstract fun draw(canvas: Canvas?)
        fun getBoxRect(imageRectWidth:Float, imageRectHeight:Float, faceBoundingBox:Rect):RectF{
            //this function calculates the size of face bounding box relative to the overlay's dimension and the dimension of the image containing the face.
            // it takes the width and height of the image rectangle(where the face bounding box is located) and face bounding box.

            val scaleX = overlay.width.toFloat() / imageRectHeight
            val scaleY = overlay.height.toFloat() / imageRectWidth
            val scale = scaleX.coerceAtLeast(scaleY)   //calculates the scaling factor to ensures that the face bounding box fits the dimension of the overlay while maintaining the correct aspect ratio.
                                                        // take the largest value to scale
            overlay.mScale = scale

            val offsetX = (overlay.width.toFloat() - ceil(imageRectHeight * scale)) / 2.0f     // these two lines calculate the horizontal and vertical offsets need to center the  scaled face bounding box within the overlay
            val offsetY = (overlay.height.toFloat() - ceil(imageRectWidth * scale)) / 2.0f

            overlay.mOffsetX = offsetX
            overlay.mOffsetY = offsetY

            val mappedBox = RectF().apply {                      //these block of code calculates the mapped coordinates of the face bounding box within the overlay
                left = faceBoundingBox.right * scale + offsetX
                top = faceBoundingBox.top * scale + offsetY
                right = faceBoundingBox.left * scale + offsetX
                bottom = faceBoundingBox.bottom * scale + offsetY
            }

            val centerX = overlay.width.toFloat() / 2

            return mappedBox.apply {           // these block of codes returns the mapped coordinates by adjusting the mapped coordinated of the face bounding box to ensure it is center horizontally within the overlay
                left = centerX + (centerX - left)
                right = centerX - (right - centerX)
            }
        }
    }

    fun clear(){              // this function responsible for clearing of the faceBoxes from the list and then invoke the postInvalidate() function.
        synchronized(lock){
            faceBoxes.clear()
        }
        postInvalidate()   // it ensures that the current view on the faceOverlay need to redrawn by discarding the older view.
    }

    fun add(faceBox: FaceBox){
        synchronized(lock){
            faceBoxes.add(faceBox)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock){
            for (graphic in faceBoxes){
                graphic.draw(canvas)
            }
        }
    }
}