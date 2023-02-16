package com.example.majika.ui.menu

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.MajikaApplication
import com.example.majika.databinding.FragmentMenuBinding
import com.example.majika.model.Menu
import com.example.majika.ui.cart.CartViewModel
import com.example.majika.ui.cart.CartViewModelFactory

class MenuFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentMenuBinding? = null
    val cartViewModel: CartViewModel by viewModels { CartViewModelFactory((this.requireActivity().application as MajikaApplication).repository) }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var fragmentFoodData: List<Menu> = emptyList()
    private var fragmentDrinksData: List<Menu> = emptyList()

    private lateinit var sensorManager : SensorManager
    private var temperature: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

//        set view in the activity
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        recycle view for menu
        val foodRv: RecyclerView = binding.foodRv
        menuViewModel.foodList.observe(viewLifecycleOwner) {
            foodRv.adapter = MenuAdapter(it.menuList!!, activity as Context, cartViewModel)
            fragmentFoodData = it.menuList
        }

        menuViewModel.getFood()

        val drinksRv: RecyclerView = binding.drinksRv
        menuViewModel.drinksList.observe(viewLifecycleOwner) {
            drinksRv.adapter = MenuAdapter(it.menuList!!, activity as Context, cartViewModel)
            fragmentDrinksData = it.menuList
        }

        menuViewModel.getDrinks()

        val menuSearch: SearchView = binding.menuSearch
        menuSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredFoodList = fragmentFoodData.filter {
                    it.name!!.contains(newText!!, true)
                }
                foodRv.adapter = MenuAdapter(filteredFoodList, activity as Context, cartViewModel)

                val filteredDrinksList = fragmentDrinksData.filter {
                    it.name!!.contains(newText!!, true)
                }
                drinksRv.adapter = MenuAdapter(filteredDrinksList, activity as Context, cartViewModel)

                return false
            }
        })

        val parentLayout: LinearLayout = binding.parentLayout
        val searchLayout: LinearLayout = binding.searchLayout

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parentLayout.orientation = LinearLayout.HORIZONTAL
        } else {
            parentLayout.orientation = LinearLayout.VERTICAL
            (searchLayout.layoutParams as LinearLayout.LayoutParams).weight = 10.0f
        }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sensorManager = this.requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        (activity as AppCompatActivity).supportActionBar?.title = "Menu"

        // untuk cek apakah ada ambient temperature sensor
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE)
        Log.v("Total sensors: ", deviceSensors.size.toString())

        deviceSensors.forEach{
            Log.v("Sensor name",""+it)
        }

        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temperature != null){
            sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this.requireActivity(), "No temperature sensor detected!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSensorChanged(event: SensorEvent){
        val temp = event.values[0]
        (activity as AppCompatActivity).supportActionBar?.title = "Menu - $temp"
        Toast.makeText(activity, "Temperature: $temp", Toast.LENGTH_SHORT).show()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}