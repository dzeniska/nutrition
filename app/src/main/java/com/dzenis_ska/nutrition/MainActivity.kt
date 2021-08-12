package com.dzenis_ska.nutrition

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.CurveGraphView
import com.broooapps.graphview.models.GraphData
import com.broooapps.graphview.models.PointMap
import com.dzenis_ska.nutrition.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var rootElement: ActivityMainBinding? = null
    private var job: Job? = null
    val listViewButtons = arrayListOf<View>()
    val listViewCl = arrayListOf<View>()
    val listViewBtnPlus = arrayListOf<View>()
    val listViewTvKcal = arrayListOf<View>()
    val listViewTvCock = arrayListOf<View>()
    val listIntTvGr = arrayListOf<Int>()
    val listViewTvGr = arrayListOf<View>()

    val dialogHelper = DialogHelper(this)
    var eating: Int = 0

    var curveGraphView: CurveGraphView? = null

    var cs = ConstraintSet()
    lateinit var anim: Animation
    lateinit var anim2: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val */rootElement = ActivityMainBinding.inflate(layoutInflater)
//        this.rootElement = rootElement
        setContentView(rootElement!!.root)

        anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha2)
        graph()

        rootElement!!.apply {

            imageButton.setOnClickListener(onCLick())
            imageButton2.setOnClickListener(onCLick())
            imageButton3.setOnClickListener(onCLick())

            listViewButtons.add(imageButton)
            listViewButtons.add(imageButton2)
            listViewButtons.add(imageButton3)

            listViewCl.add(cl1)
            listViewCl.add(cl2)
            listViewCl.add(cl3)

            listViewBtnPlus.add(imbtn1)
            listViewBtnPlus.add(imbtn2)
            listViewBtnPlus.add(imbtn3)

            listViewTvKcal.add(tvInputKcal1)
            listViewTvKcal.add(tvInputKcal2)
            listViewTvKcal.add(tvInputKcal3)

            listViewTvCock.add(tvCock1)
            listViewTvCock.add(tvCock2)
            listViewTvCock.add(tvCock3)

            listIntTvGr.add(R.id.tvGr1)
            listIntTvGr.add(R.id.tvGr2)
            listIntTvGr.add(R.id.tvGr3)

            listViewTvGr.add(tvGr1)
            listViewTvGr.add(tvGr2)
            listViewTvGr.add(tvGr3)

        }
    }

    private fun onCLick(): View.OnClickListener? {
        return View.OnClickListener { view ->
//            Log.d("!!!", "${job}")
            if (job == null) {
                job = CoroutineScope(Dispatchers.Main).launch {
//                    for(item in 0..listViewButtons.size)
                    val index = listViewButtons.indexOf(view)
                    listViewButtons.forEach {
                        if (it != view) {
                            val pos = listViewButtons.indexOf(it)
                            if (it.elevation != 0.0f) elevateDown(it, listViewCl[pos])
                        }
                    }
                    listViewBtnPlus.forEach {v->
                        if(listViewBtnPlus[index] == v){
                            v.visibility = View.VISIBLE
                        }else{
                            v.visibility = View.GONE
                        }
                    }
                    listViewBtnPlus[index].setOnClickListener(btnAddKcal(index))
                    if (view.elevation == 0.0f) {
                        elevateUp(view, listViewCl[index])
                    } else {
                        elevateDown(view, listViewCl[index])
                    }
                    job = null
                }
            }
        }
    }

    private fun btnAddKcal(index: Int): View.OnClickListener? {
        return View.OnClickListener {
            dialogHelper.createDialog(index)
        }

    }


    private suspend fun elevateDown(view: View, viewCl: View) = withContext(Dispatchers.IO) {
        var el = view.elevation
//        var elev = (el * 10).toInt()
        Log.d("!!!elevDown", "${el}")
        Log.d("!!!elevDown", "${view.translationY}")

        for (i in 20 downTo 1) {
            el -= 2.0f
            view.elevation = el/*ev.toFloat() / 10*/
            view.translationY = -el/2/*ev.toFloat() / 20*/
            viewCl.elevation = el.plus(1.0f)/*ev.toFloat() / 10*/
            viewCl.translationY = -el/2/*ev.toFloat() / 20*/
            Log.d("!!!elevDd2", "${ -el}")
            Log.d("!!!elevDd2", "${ el/2}")
//            elev -= 10
            delay(10)
        }
        Log.d("!!!elevDown", "${ view.elevation}")
        Log.d("!!!elevDown", "${ view.translationY}")
        Log.d("!!!elevDown", "${ viewCl.elevation}")
        Log.d("!!!elevDown", "${ viewCl.translationY}")
        view.elevation = 0.0f
        view.translationY = 0.0f
        viewCl.elevation = 0.0f
        viewCl.translationY = 0.0f


        //тут косяк с alpha значением

//        var alpha = 1.0f
//        for (i in 5 downTo 0) {
//            view.alpha = alpha
//            alpha -= 0.1f
//            Log.d("!!!alpha", "${alpha}")
//            delay(4)
//        }
//            Log.d("!!!alpha", "${alpha}")

        val img = view as ImageButton
        img.background = resources.getDrawable(R.drawable.img_btn_back_normal)

//        for (i in 10 downTo 1) {
//            view.alpha = alpha
//            alpha += 0.02f
//            delay(2)
//        }
//        Log.d("!!!alpha", "${alpha}")
////            Log.d("!!!", "${alpha}")
//        view.alpha = 1.0f
    }

    private suspend fun elevateUp(view: View, viewCl: View) = withContext(Dispatchers.IO) {
        var el = view.elevation

//        var elev = (el * 10).toInt()
        Log.d("!!!elevUp", "${ view.elevation}")
        Log.d("!!!elevUp", "${ view.translationY}")

        view.background =
            resources.getDrawable(R.drawable.img_btn_back_pressed)
//        var alpha = 0.8f
        for (i in 0..40) {
//            Log.d("!!!eleV", "${elev.toFloat()/10}")
            viewCl.elevation = /*(*/el.plus(1.0f)/*ev.toFloat() / 10).plus(2)*/
            view.elevation = el/*ev.toFloat() / 10*/
            viewCl.translationY = -el/2/*ev.toFloat() / 20*/
            view.translationY = -el/2/*ev.toFloat() / 20*/
//            Log.d("!!!eleC", "${elev.toFloat()/10}")


            Log.d("!!!elevUd2", "${ -el/2}")
            Log.d("!!!elevUd2", "${ el}")
//            alpha += 0.005f
//            view.alpha = alpha
//            elev += 10
            el += 1.0f
            delay(10)
        }
        Log.d("!!!elevUp", "${ view.elevation}")
        Log.d("!!!elevUp", "${ view.translationY}")


    }

    fun showResult(index: Int, text: String) {


        Log.d("!!!y", "${rootElement!!.tvGr1.y}")
//        rootElement!!.tvGr1.y = 500.0f

        rootElement!!.apply {
//            tvInputKcal1.text = text
            val view = listViewTvKcal[index] as TextView
            val viewGr = listViewTvGr[index] as TextView
            view.text = text
            viewGr.text = text

            eating = 0
            for ((ind, element) in listViewTvKcal.withIndex()) {
                val el = element as TextView
                val sum = el.text.toString().toInt()
                eating += sum
            }


            //можно и viewModel сделать

            val listHeight = arrayListOf<Int>()

            listHeight.add(tvInputKcal1.text.toString().toInt())
            listHeight.add(tvInputKcal2.text.toString().toInt())
            listHeight.add(tvInputKcal3.text.toString().toInt())

            // узнаём максимальную высоту
            var maxHeight = 0
            listHeight.forEach { height ->
                if(height > maxHeight) maxHeight = height
            }

            val heightListNormal = arrayListOf<Float>()

            if(maxHeight > MAX_HEIGHT){
                val del = (MAX_HEIGHT).toFloat()/ (maxHeight).toFloat()
//                разница
                var indexL = 0
                listHeight.forEach {
                    val h = it.toFloat()*del

                    heightListNormal.add(h)
                    val hInt = h.toInt()
                    Log.d("!!!h", "$hInt")
                    cs.clone(rootElement!!.constrLayoutGraph)
                    cs.connect(listIntTvGr[indexL], ConstraintSet.BOTTOM, R.id.constrLayoutGraph, ConstraintSet.BOTTOM, (hInt*0.60f).toInt())
//        cs.setVerticalBias(R.id.tvGr1, 0.5f)
                    cs.applyTo(rootElement!!.constrLayoutGraph)
                    indexL++
                }
                pointMap(heightListNormal[0].toInt(), heightListNormal[1].toInt(), heightListNormal[2].toInt())
            }else{
                var indexL = 0
                listHeight.forEach {
                    heightListNormal.add(it.toFloat())
                    Log.d("!!!h", "$it")
                    cs.clone(rootElement!!.constrLayoutGraph)
                    cs.connect(listIntTvGr[indexL], ConstraintSet.BOTTOM, R.id.constrLayoutGraph, ConstraintSet.BOTTOM, (it*0.60f).toInt())
//        cs.setVerticalBias(R.id.tvGr1, 0.5f)
                    cs.applyTo(rootElement!!.constrLayoutGraph)
                    indexL++
                }
                pointMap(heightListNormal[0].toInt(), heightListNormal[1].toInt(), heightListNormal[2].toInt())
            }
//            сделать текст bias

//            var kcalGraph = text.toInt()
//            if(kcalGraph > MAX_HEIGHT){
//                kcalGraph = MAX_HEIGHT
//
//            }
//            pointMap(kcalGraph, 300, 890)

            val viewClock = listViewTvCock[index] as TextView
            val dataFormat = SimpleDateFormat("h:mm a")
            viewClock.text = dataFormat.format(Calendar.getInstance().time)

            tvEatingRes.text = "${eating} kcal"
            val burnt = tvBurntRes.text.toString()
            val total = eating - burnt.substringBefore(" ").toInt()
//            Log.d("!!!", "${burnt.substringBefore(" ").toInt()}")
            tvTotalRes.text = "${total}"

        }
    }

    override fun onDestroy() {
        rootElement = null
        super.onDestroy()
    }

    fun graph() {
        curveGraphView = findViewById(R.id.cgv)

        val conf = curveGraphView!!.configure(
            CurveGraphConfig.Builder(this)
                .setAxisColor(R.color.invisible)                                             // Set number of values to be displayed in X ax
//                .setVerticalGuideline(4)                                                // Set number of background guidelines to be shown.
//                .setHorizontalGuideline(2)
                .setGuidelineColor(R.color.invisible)                                         // Set color of the visible guidelines.
                .setNoDataMsg(" No Data ")                                              // Message when no data is provided to the view.
                .setxAxisScaleTextColor(R.color.invisible)                                  // Set X axis scale text color.
                .setyAxisScaleTextColor(R.color.invisible)                                  // Set Y axis scale text color
//                .setAnimationDuration(2000)
                .build()
        )


//        pointMap(50, 60, 30)
    }

    private fun pointMap(v1: Int, v2: Int, v3: Int) {
        val pointMap = PointMap()
        pointMap.apply {
            addPoint(0, 0)
            addPoint(2, v1)
            addPoint(3, 0)
            addPoint(4, v2)
            addPoint(5, 0)
            addPoint(6, v3)
            addPoint(7, 0)
        }

//        val listGrapf = arrayListOf<GraphData>()
//        for(i in -7..7){
//            Log.d("!!!", "${i}")
//            listGrapf.add(pointNextMap(i, v1.plus(i), v2.plus(i), v3.plus(i)))
//        }

        val gd: GraphData = GraphData.builder(this)
            .setPointMap(pointMap)
            .setGraphStroke(R.color.bir)
            .setPointColor(R.color.black)
            .setPointRadius(5)
            .build()
//        listGrapf.add(gd)


        curveGraphView!!.setData(7, 1000, gd)

//        curveGraphView!!.setData(7, 1000,
//            listGrapf[0],
//            listGrapf[1],
//            listGrapf[2],
//            listGrapf[3],
//            listGrapf[4],
//            listGrapf[5],
//            listGrapf[6],
//            listGrapf[7],
//            listGrapf[8],
//            listGrapf[9],
//            listGrapf[10],
//            listGrapf[11],
//            listGrapf[12],
//            listGrapf[13],
//            listGrapf[14],
//            listGrapf[15]
//        )
    }

//    private fun pointNextMap(v0: Int, v1: Int, v2: Int, v3: Int): GraphData {
//        val pointMap2 = PointMap()
//        pointMap2.apply {
//            addPoint(0, v0)
//            addPoint(2, v1)
//            addPoint(3, v0)
//            addPoint(4, v2)
//            addPoint(5, v0)
//            addPoint(6, v3)
//            addPoint(7, v0)
//        }
//        val gd2: GraphData = GraphData.builder(this)
//            .setPointMap(pointMap2)
//            .setGraphStroke(R.color.bir)
//            .setPointColor(R.color.invisible)
//            .setPointRadius(0)
//            .build()
//        return gd2
//
//    }

    companion object {
        const val MAX_HEIGHT = 500
    }
}