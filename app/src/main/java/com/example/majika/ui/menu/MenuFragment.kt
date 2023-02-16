package com.example.majika.ui.menu

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.MainActivity
import com.example.majika.MajikaApplication
import com.example.majika.R
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
    private var temp: Sensor? = null

    private lateinit var foodAdapter: MenuAdapter
    private lateinit var drinksAdapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        set view in the activity
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        recycle view for menu
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

        val foodRv: RecyclerView = binding.foodRv
        foodAdapter = MenuAdapter(activity as Context, cartViewModel)
        foodRv.layoutManager = LinearLayoutManager(this.requireContext())
        foodRv.adapter = foodAdapter

        menuViewModel.foodList.observe(viewLifecycleOwner) {
            foodAdapter.setMenuData(it.menuList!!)
            fragmentFoodData = it.menuList
        }

        menuViewModel.getFood()

        val drinksRv: RecyclerView = binding.drinksRv
        drinksAdapter = MenuAdapter(activity as Context, cartViewModel)
        drinksRv.layoutManager = LinearLayoutManager(this.requireContext())
        drinksRv.adapter = foodAdapter
        menuViewModel.drinksList.observe(viewLifecycleOwner) {
            drinksAdapter.setMenuData(it.menuList!!)
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
                foodAdapter.setMenuData(filteredFoodList)

                val filteredDrinksList = fragmentDrinksData.filter {
                    it.name!!.contains(newText!!, true)
                }
                drinksAdapter.setMenuData(filteredDrinksList)

                return false
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sensorManager = this.requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (temp != null){
            sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this.requireActivity(), "No temperature sensor detected!", Toast.LENGTH_SHORT).show()
        }

        (activity as MainActivity).setTitle(getString(R.string.title_menu))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSensorChanged(event: SensorEvent) {
        val temp = event.values[0]
        val view = (activity as AppCompatActivity).supportActionBar?.customView

        val temperatureText: TextView = view!!.findViewById(R.id.temperature_text)
        temperatureText.text = getString(R.string.temperature, temp)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}